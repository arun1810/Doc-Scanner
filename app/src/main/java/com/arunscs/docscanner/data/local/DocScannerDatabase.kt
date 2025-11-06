package com.arunscs.docscanner.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [LocalDocument::class], version = 1,exportSchema = false)
@TypeConverters(TypeConverter::class)
abstract class DocScannerDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
}
