package br.com.geanbrandao.mockinterceptorforum

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import br.com.geanbrandao.mockinterceptorforum.databinding.ActivityMainBinding
import br.com.geanbrandao.mockinterceptorforum.domain.State
import br.com.geanbrandao.mockinterceptorforum.domain.model.OneLaunchModel
import br.com.geanbrandao.mockinterceptorforum.utils.toYesOrNo
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btAgain.setOnClickListener {
            getOneLaunch(67)
            binding.clError.isVisible = false
        }
        getOneLaunch(967)
    }

    private fun getOneLaunch(flightNumber: Int) {
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.getOnLaunch(flightNumber, this@MainActivity).collect { response ->
                    when (response) {
                        is State.LoadingState -> {
                            handleLoading(response.isLoading)
                        }
                        is State.DataState -> {
                            response.data?.let { handleSuccess(it) }
                        }
                        is State.ErrorState -> {
                            handleError(response.exception.message.orEmpty())
                        }
                    }
                }
            }
        }
    }

    private fun handleError(message: String) {
        binding.clError.isVisible = true
        binding.tvError.text = message
        binding.svContent.isVisible = false
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.pbLoading.isVisible = isLoading
    }

    private fun handleSuccess(oneLaunchModel: OneLaunchModel) = with(binding) {
        Glide.with(this@MainActivity).load(oneLaunchModel.links.flickr_images[0]).into(ivHeader)
        tvMissionName.text = oneLaunchModel.mission_name
        tvFlightNumber.text = oneLaunchModel.flight_number.toString()
        tvLaunchDate.text = oneLaunchModel.launch_date_local.substringBefore('T')
        tvSuccess.text = oneLaunchModel.launch_success.toYesOrNo()
        tvDetails.text = oneLaunchModel.details
        binding.svContent.isVisible = true
    }
}