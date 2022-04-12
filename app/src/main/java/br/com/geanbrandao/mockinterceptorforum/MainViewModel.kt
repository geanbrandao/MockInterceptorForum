package br.com.geanbrandao.mockinterceptorforum

import android.content.Context
import androidx.lifecycle.ViewModel
import br.com.geanbrandao.mockinterceptorforum.data.NetworkUtils
import br.com.geanbrandao.mockinterceptorforum.domain.State
import br.com.geanbrandao.mockinterceptorforum.utils.NetworkErrorException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class MainViewModel : ViewModel() {

    fun getOnLaunch(flightNumber: Int, context: Context) = flow {
        emit(State.LoadingState(isLoading = true))

        try {
            val response = NetworkUtils.createApiInstance(context).getOneLaunch(flightNumber)
                emit(State.DataState(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(resolveError(e))
        } finally {
            emit(State.LoadingState(isLoading = false))
        }
    }

    private fun resolveError(e: Exception): State.ErrorState {

        val error = when (e) {
            is SocketTimeoutException -> {
                NetworkErrorException(errorMessage = "Connection error!")
            }
            is ConnectException -> {
                NetworkErrorException(errorMessage = "No internet access!")
            }
            is UnknownHostException -> {
                NetworkErrorException(errorMessage = "No internet access!")
            }
            is HttpException -> {
                when (e.code()) {
                    502 -> NetworkErrorException(e.code(),  "Internal error!")
                    404 -> NetworkErrorException(e.code(), "Path not found!")
                    400 -> NetworkErrorException.parseException(e)
                    else -> e
                }
            }
            else -> e
        }
        return State.ErrorState(error)
    }
}