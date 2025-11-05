package com.example.aptekaspm_mobile.data.network.models

import com.google.gson.annotations.SerializedName

data class RestockRequest(
    @SerializedName("GID")
    val gid: String,
    @SerializedName("SN")
    val sn: String,
    val transferAmount: Int,
    @SerializedName("medkit_id")
    val medkitId: Int
)
