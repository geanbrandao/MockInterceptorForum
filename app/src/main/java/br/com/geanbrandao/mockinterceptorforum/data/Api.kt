package br.com.geanbrandao.mockinterceptorforum.data

import br.com.geanbrandao.mockinterceptorforum.domain.model.OneLaunchModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @GET("v3/launches/{flight_number}")
    suspend fun getOneLaunch(@Path("flight_number") flight_number: Int): OneLaunchModel
}