package com.example.bookingappassignment.data.source

class LocationDataSourceImpl(private val gpsTracker: GpsTracker) : LocationDataSource {
    override suspend fun getCurrentLocation() = gpsTracker.getCurrentLocation()
}