package com.example.aptekaspm_mobile.data.network

import com.example.aptekaspm_mobile.data.network.models.CrewRequest
import com.example.aptekaspm_mobile.data.network.models.CrewResponse
import com.example.aptekaspm_mobile.data.network.models.MedicationInfoRequest
import com.example.aptekaspm_mobile.data.network.models.MedicationInfoResponse
import com.example.aptekaspm_mobile.data.network.models.ReceiveLogItem
import com.example.aptekaspm_mobile.data.network.models.ReceiveRequest
import com.example.aptekaspm_mobile.data.network.models.RestockLogItem
import com.example.aptekaspm_mobile.data.network.models.RestockRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("medication-information")
    suspend fun getMedicationInfo(@Body request: MedicationInfoRequest): Response<MedicationInfoResponse>

    @POST("receive")
    suspend fun receiveMedication(@Body request: ReceiveRequest): Response<Unit>

    @POST("restock")
    suspend fun restockMedkit(@Body request: RestockRequest): Response<Unit>

    @POST("crew-number")
    suspend fun getCrewNumber(@Body request: CrewRequest): Response<CrewResponse>

    @GET("logs/restock")
    suspend fun getRestockLogs(): Response<List<RestockLogItem>>

    @GET("logs/receive")
    suspend fun getReceiveLogs(): Response<List<ReceiveLogItem>>

}
