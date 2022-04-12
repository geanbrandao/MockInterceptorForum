package br.com.geanbrandao.mockinterceptorforum.data

import android.content.Context
import com.google.gson.GsonBuilder
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
        }.build()
    }

    private fun createLogInterceptor() = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    fun createApiInstance(context: Context): Api =
        createRetrofitInstance(context = context).create(Api::class.java)
}