package com.example.aptekaspm_mobile.data.network.models

data class DispensingLogItem(
    val boxId: Int,
    val medkitId: Int,
    val transferAmount: Int,
    val transferDate: String,
    val medicationName: String,
    val gid: String,
    val sn: String,
    val expiryDate: String
)

data class ReceivingLogItem(
    val boxId: Int,
    val receiveDate: String,
    val medicationName: String,
    val gid: String,
    val sn: String,
    val expiryDate: String
)
