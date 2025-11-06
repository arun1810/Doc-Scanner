package com.arunscs.docscanner.ui.util

class UiDateUtil {
    companion object {
        // Write a method to convert a date time object to a formatted string as, Mon 01 Jan 2024, 10:30 AM
        fun formatDateTimeToString(dateTime: java.time.LocalDateTime): String {
            val formatter = java.time.format.DateTimeFormatter.ofPattern("EEE dd MMM yyyy, hh:mm a")
            return dateTime.format(formatter)
        }
    }
}