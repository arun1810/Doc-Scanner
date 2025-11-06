package com.arunscs.docscanner.domain.repository

import com.arunscs.docscanner.domain.entity.Document
import kotlinx.coroutines.flow.Flow

interface DocumentRepository {

    suspend fun getAllDocuments(): Flow<List<Document>>

    suspend fun addDocument(document: Document)

    suspend fun getDocumentById(documentId: String): Document?

}