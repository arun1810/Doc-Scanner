package com.arunscs.docscanner.core.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

const val INTERNAL_DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm"

class DateUtils {
    companion object{

        val internalDateTimeFormat: DateTimeFormatter by lazy{
            DateTimeFormatter.ofPattern(INTERNAL_DATE_TIME_PATTERN)
        }

        fun convertStringToDateTime(str:String): LocalDateTime {
            return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(INTERNAL_DATE_TIME_PATTERN))
        }

        fun convertDateTimeToString(date: LocalDateTime):String{
            return date.format(DateTimeFormatter.ofPattern(INTERNAL_DATE_TIME_PATTERN))
        }
    }
}