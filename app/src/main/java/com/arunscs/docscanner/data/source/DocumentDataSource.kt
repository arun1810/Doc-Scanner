package com.arunscs.docscanner.data.source

import com.arunscs.docscanner.domain.entity.Document
import kotlinx.coroutines.flow.Flow

interface DocumentDataSource {
    //Add datasource methods for document data class
    suspend fun getAllDocuments(): Flow<List<Document>>
    suspend fun getDocumentById(documentId: String): Document?
    suspend fun insertDocument(document: Document)
    suspend fun deleteDocumentById(document: Document)
}