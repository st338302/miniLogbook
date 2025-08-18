package com.roche.minilogbook.di

import android.content.Context
import androidx.room.Room
import com.roche.minilogbook.data.local.LogBookDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): LogBookDataBase =
        Room.databaseBuilder(context, LogBookDataBase::class.java, "minilogbook-db").build()

    @Provides
    @Singleton
    fun provideLogBookDao(logDataBase: LogBookDataBase) = logDataBase.logDao()
}