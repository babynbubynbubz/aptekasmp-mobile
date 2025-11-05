package com.example.aptekaspm_mobile.data.network.models

import com.google.gson.annotations.SerializedName

data class CrewRequest(
    @SerializedName("medkit_id")
    val medkitId: Int
)

data class CrewResponse(
    val crewId: Int
)
