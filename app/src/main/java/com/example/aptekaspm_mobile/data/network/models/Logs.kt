package com.example.aptekaspm_mobile.data.network.models

data class DispensingLogItem(
    val boxId: Int,
    val medkitId: Int,
    val transferAmount: Int,
    val transferDate: String, // Assuming ISO 8601 date format
    val medicationName: String,
    val gid: String,
    val sn: String,
    val expiryDate: String // Assuming ISO 8601 date format
)

data class ReceivingLogItem(
    val boxId: Int,
    val receiveDate: String, // Assuming ISO 8601 date format
    val medicationName: String,
    val gid: String,
    val sn: String,
    val expiryDate: String // Assuming ISO 8601 date format
)