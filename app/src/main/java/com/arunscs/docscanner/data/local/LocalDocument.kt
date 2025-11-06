package com.arunscs.docscanner.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arunscs.docscanner.domain.entity.Document
import java.time.LocalDateTime

@Entity(tableName = "documents")
data class LocalDocument(
    @PrimaryKey val id:String,
    @ColumnInfo(name="title") val title:String,
    @ColumnInfo(name="created_on") val createdOn: LocalDateTime,
)
