package com.roche.minilogbook.di

import com.roche.minilogbook.data.repository.LogRepository
import com.roche.minilogbook.data.local.LogBookDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideLogRepository(dao: LogBookDao): LogRepository = LogRepository(dao)
}