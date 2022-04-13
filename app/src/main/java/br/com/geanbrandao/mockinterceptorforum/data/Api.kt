package br.com.geanbrandao.mockinterceptorforum.data

import br.com.geanbrandao.mockinterceptorforum.domain.model.OneLaunchModel
import com.gustafah.android.mockinterceptor.Mock
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {

    @Mock("v3_launches_67_GET.json")
    @GET("v3/launches/{flight_number}")
    suspend fun getOneLaunch(@Path("flight_number") flight_number: Int): OneLaunchModel
}