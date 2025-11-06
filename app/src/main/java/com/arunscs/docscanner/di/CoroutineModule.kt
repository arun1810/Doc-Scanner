package com.arunscs.docscanner.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IODispatcher

@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @IODispatcher
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO
}