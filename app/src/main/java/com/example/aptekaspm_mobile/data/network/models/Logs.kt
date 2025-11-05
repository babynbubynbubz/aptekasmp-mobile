package com.example.aptekaspm_mobile.data.network.models

import com.google.gson.annotations.SerializedName

data class RestockLogItem(
    @SerializedName("box_id")
    val boxId: Int,
    @SerializedName("medkit_id")
    val medkitId: Int,
    val transferAmount: Int,
    val transferDate: String, // Assuming ISO 8601 date format
    val medicationName: String,
    @SerializedName("SN")
    val sn: String,
    val expiryDate: String // Assuming ISO 8601 date format
)

data class ReceiveLogItem(
    @SerializedName("box_id")
    val boxId: Int,
    val receiveDate: String, // Assuming ISO 8601 date format
    val medicationName: String,
    @SerializedName("SN")
    val sn: String,
    val expiryDate: String // Assuming ISO 8601 date format
)
