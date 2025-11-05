package com.example.aptekaspm_mobile.data.network.models

import com.google.gson.annotations.SerializedName

data class ReceiveRequest(
    @SerializedName("GID")
    val gid: String,
    @SerializedName("SN")
    val sn: String,
    val expiryDate: String
)
