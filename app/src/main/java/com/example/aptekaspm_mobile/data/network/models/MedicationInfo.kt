package com.example.aptekaspm_mobile.data.network.models

import com.google.gson.annotations.SerializedName

data class MedicationInfoRequest(
    val scanData: String
)

data class MedicationInfoResponse(
    val info: MedicationDetails,
    val storageInfo: StorageInfo?
)

data class MedicationDetails(
    val name: String,
    @SerializedName("INN")
    val inn: String,
    val inBoxAmount: Int,
    @SerializedName("GID")
    val gid: String,
    @SerializedName("SN")
    val sn: String
)

data class StorageInfo(
    val inBoxRemaining: Int,
    val expiryDate: String // Assuming ISO 8601 date format as String
)
