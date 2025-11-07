package com.example.aptekaspm_mobile.ui.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun formatDate(dateString: String): String {
    return try {
        val offsetDateTime = OffsetDateTime.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        offsetDateTime.format(formatter)
    } catch (e: Exception) {
        dateString // Return original string if parsing fails
    }
}
