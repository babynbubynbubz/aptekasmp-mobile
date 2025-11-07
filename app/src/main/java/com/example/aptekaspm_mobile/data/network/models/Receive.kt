package com.example.aptekaspm_mobile.data.network.models

data class ReceiveRequest(
    val scanData: String,
    val expiryDate: String
)

data class ReceiveResponse(
    val success: Boolean,
    val message: String
)