package com.example.aptekaspm_mobile.ui.navigation

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Main : Screen("main_screen")
    object Logs : Screen("logs_screen")

    object Receive : Screen("receive_screen/{scanData}/{gid}/{sn}/{name}/{inn}/{inBoxAmount}") {
        fun createRoute(
            scanData: String,
            gid: String,
            sn: String,
            name: String,
            inn: String,
            inBoxAmount: Int
        ): String {
            val encodedScanData = URLEncoder.encode(scanData, StandardCharsets.UTF_8.toString())
            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
            val encodedInn = URLEncoder.encode(inn, StandardCharsets.UTF_8.toString())
            return "receive_screen/$encodedScanData/$gid/$sn/$encodedName/$encodedInn/$inBoxAmount"
        }
    }

    object Dispense :
        Screen("dispense_screen/{scanData}/{gid}/{sn}/{name}/{inn}/{inBoxAmount}/{remainingAmount}/{expiryDate}") {
        fun createRoute(
            scanData: String,
            gid: String,
            sn: String,
            name: String,
            inn: String,
            inBoxAmount: Int,
            remainingAmount: Int,
            expiryDate: String
        ): String {
            val encodedScanData = URLEncoder.encode(scanData, StandardCharsets.UTF_8.toString())
            val encodedName = URLEncoder.encode(name, StandardCharsets.UTF_8.toString())
            val encodedInn = URLEncoder.encode(inn, StandardCharsets.UTF_8.toString())
            val encodedExpiry = URLEncoder.encode(expiryDate, StandardCharsets.UTF_8.toString())
            return "dispense_screen/$encodedScanData/$gid/$sn/$encodedName/$encodedInn/$inBoxAmount/$remainingAmount/$encodedExpiry"
        }
    }
}