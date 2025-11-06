package com.arunscs.docscanner.data.local

import com.arunscs.docscanner.data.source.DocumentDataSource
import com.arunscs.docscanner.data.source.toDomainModel
import com.arunscs.docscanner.data.source.toLocalModel
import com.arunscs.docscanner.di.IODispatcher
import com.arunscs.docscanner.domain.entity.Document
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DocumentLocalDataSource @Inject constructor (
    private val documentDao: DocumentDao,
   @IODispatcher private val ioDispatcher: CoroutineDispatcher
): DocumentDataSource {
    override suspend fun getAllDocuments(): Flow<List<Document>> {
        return documentDao.getAllDocuments()
            .map { it.toDomainModel() }
            .flowOn(ioDispatcher)
    }

    override suspend fun getDocumentById(documentId: String): Document? = withContext(ioDispatcher) {
        documentDao.getDocumentById(documentId)?.toDomainModel()
    }

    override suspend fun insertDocument(document: Document) = withContext(ioDispatcher) {
        documentDao.insertDocument(document.toLocalModel())
    }

    override suspend fun deleteDocumentById(document:Document) = withContext(ioDispatcher) {
        documentDao.deleteDocument(document.toLocalModel())
    }
}