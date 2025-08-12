package com.example.bookingappassignment.di

import android.content.Context
import com.example.bookingappassignment.data.source.GpsTracker
import com.example.bookingappassignment.data.source.LocationDataSource
import com.example.bookingappassignment.data.source.LocationDataSourceImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Singleton
    @Provides
    fun provideGpsTracker(
        @ApplicationContext context: Context
    ): GpsTracker {
        return GpsTracker(context)
    }

    @Singleton
    @Provides
    fun provideLocationDataSource(
        gpsTracker: GpsTracker
    ): LocationDataSource {
        return LocationDataSourceImpl(gpsTracker)

    }
}