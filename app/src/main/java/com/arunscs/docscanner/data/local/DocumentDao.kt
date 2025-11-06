package com.arunscs.docscanner.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    //Add Dao methods for LocalDocument entity
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDocument(document: LocalDocument)

    @Query("SELECT * FROM documents WHERE id = :id")
    suspend fun getDocumentById(id: String): LocalDocument?

    @Query("SELECT * FROM documents ORDER BY created_on DESC")
    fun getAllDocuments(): Flow<List<LocalDocument>>

    @Delete
    suspend fun deleteDocument(document: LocalDocument)

}