package com.example.aptekaspm_mobile.data.network

import com.example.aptekaspm_mobile.data.network.models.CrewRequest
import com.example.aptekaspm_mobile.data.network.models.CrewResponse
import com.example.aptekaspm_mobile.data.network.models.DispenseRequest
import com.example.aptekaspm_mobile.data.network.models.DispenseResponse
import com.example.aptekaspm_mobile.data.network.models.DispensingLogItem
import com.example.aptekaspm_mobile.data.network.models.MedicationInfoRequest
import com.example.aptekaspm_mobile.data.network.models.MedicationInfoResponse
import com.example.aptekaspm_mobile.data.network.models.ReceiveRequest
import com.example.aptekaspm_mobile.data.network.models.ReceiveResponse
import com.example.aptekaspm_mobile.data.network.models.ReceivingLogItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/Pharmacy/medication-info")
    suspend fun getMedicationInfo(@Body request: MedicationInfoRequest): Response<MedicationInfoResponse>

    @POST("api/Pharmacy/receive")
    suspend fun receiveMedication(@Body request: ReceiveRequest): Response<ReceiveResponse>

    @POST("api/Pharmacy/dispense")
    suspend fun dispenseMedication(@Body request: DispenseRequest): Response<DispenseResponse>

    @POST("api/Pharmacy/crew-info")
    suspend fun getCrewInfo(@Body request: CrewRequest): Response<CrewResponse>

    @GET("api/Pharmacy/dispensing-logs")
    suspend fun getDispensingLogs(): Response<List<DispensingLogItem>>

    @GET("api/Pharmacy/receiving-logs")
    suspend fun getReceivingLogs(): Response<List<ReceivingLogItem>>

}