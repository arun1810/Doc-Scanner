package com.arunscs.docscanner.data.local

import androidx.room.TypeConverter
import com.arunscs.docscanner.core.util.DateUtils
import java.time.LocalDateTime

class TypeConverter {

    @TypeConverter
    fun fromLocalDateTimeToTimeStamp(dateTime: LocalDateTime):String{
        return dateTime.format(DateUtils.internalDateTimeFormat)
    }

    @TypeConverter
    fun fromTimeStampToLocalDateTime(timeStamp:String):LocalDateTime{
        return LocalDateTime.parse(timeStamp,DateUtils.internalDateTimeFormat)
    }
}