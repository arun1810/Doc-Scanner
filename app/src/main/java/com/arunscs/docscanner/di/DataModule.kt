package com.arunscs.docscanner.di

import android.content.Context
import androidx.room.Room
import com.arunscs.docscanner.data.local.DocScannerDatabase
import com.arunscs.docscanner.data.local.DocumentDao
import com.arunscs.docscanner.data.local.DocumentLocalDataSource
import com.arunscs.docscanner.data.repository.DocumentRepositoryImpl
import com.arunscs.docscanner.data.source.DocumentDataSource
import com.arunscs.docscanner.domain.repository.DocumentRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {

    @Provides
    @Singleton
    fun provideDocScannerDb(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        DocScannerDatabase::class.java,"DocScanner.db"
        )
        .build()

    @Provides
    fun provideDocumentDao(database: DocScannerDatabase): DocumentDao = database.documentDao()
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class LocalChatDataSource

@Module
@InstallIn(SingletonComponent::class)
abstract class DocumentRepoModule {

    @Binds
    @Singleton
    abstract fun bindDocumentRepo(documentRepoImpl: DocumentRepositoryImpl): DocumentRepository

    @Binds
    @Singleton
    @LocalChatDataSource
    abstract fun bindDocumentDataSource(documentDataSourceImpl: DocumentLocalDataSource): DocumentDataSource
}