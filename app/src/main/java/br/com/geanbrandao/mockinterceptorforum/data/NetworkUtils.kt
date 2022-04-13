package br.com.geanbrandao.mockinterceptorforum.data

import android.content.Context
import br.com.geanbrandao.mockinterceptorforum.BuildConfig
import com.google.gson.GsonBuilder
import com.gustafah.android.mockinterceptor.MockConfig
import com.gustafah.android.mockinterceptor.MockInterceptor
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkUtils {

    private const val BASE_URL = "https://api.spacexdata.com/"

    private fun createRetrofitInstance(context: Context): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(createClient(context))
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()


    private fun createClient(context: Context): OkHttpClient {
        return OkHttpClient().newBuilder().apply {
            addInterceptor(createLogInterceptor())
            if (BuildConfig.DEBUG) {
                addInterceptor(provideMockInterceptor(context))
            }
        }.build()
    }

    private fun createLogInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun provideMockInterceptor(context: Context): MockInterceptor {
        return MockInterceptor.apply {
            config = MockConfig.Builder()
                .suffix(".json")
                .separator("_")
                .context { context }
                .selectorMode(MockConfig.OptionsSelectorMode.ALWAYS_ON_TOP)
                .build()
        }
    }

    fun createApiInstance(context: Context): Api =
        createRetrofitInstance(context = context).create(Api::class.java)
}