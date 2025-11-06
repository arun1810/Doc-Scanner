package com.arunscs.docscanner.data.repository

import com.arunscs.docscanner.data.local.DocumentLocalDataSource
import com.arunscs.docscanner.domain.entity.Document
import com.arunscs.docscanner.domain.repository.DocumentRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
    private val documentLocalDataSource: DocumentLocalDataSource,
):DocumentRepository {
    override suspend fun getAllDocuments(): Flow<List<Document>> {
       return documentLocalDataSource.getAllDocuments()
    }

    override suspend fun addDocument(document: Document) {
        documentLocalDataSource.insertDocument(document)
    }

    override suspend fun getDocumentById(documentId: String): Document? {
       return documentLocalDataSource.getDocumentById(documentId)
    }
}