package com.nenadic.cdapp.data.di

import android.app.Application
import androidx.room.Room
import com.nenadic.cdapp.data.data_source.CDDatabase
import com.nenadic.cdapp.data.data_source.CDDatabase.Companion.DB_NAME
import com.nenadic.cdapp.data.repository.CDRepositoryImpl
import com.nenadic.cdapp.domian.repository.CDRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDataBase(app: Application): CDDatabase {
        return Room.databaseBuilder(
            app,
            CDDatabase::class.java, DB_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideCDRepository(db: CDDatabase): CDRepository {
        return CDRepositoryImpl(db.cdDao)
    }
}