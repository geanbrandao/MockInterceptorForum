package br.com.geanbrandao.mockinterceptorforum.domain.model

data class OneLaunchModel(
    val details: String,
    val flight_number: Int,
    val launch_date_local: String,
    val launch_success: Boolean,
    val links: Links,
    val mission_name: String,
    val rocket: Rocket
)