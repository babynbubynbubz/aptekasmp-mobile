package com.example.aptekaspm_mobile.data.network.models

data class DispenseRequest(
    val scanData: String,
    val transferAmount: Int,
    val medkitId: Int
)

data class DispenseResponse(
    val success: Boolean,
    val message: String
)
