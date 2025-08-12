package com.example.bookingappassignment.data.repository

import android.location.Location
import com.example.bookingappassignment.data.model.Driver
import com.example.bookingappassignment.data.source.DriverDataSource
import com.example.bookingappassignment.data.source.LocationDataSource
import com.example.bookingappassignment.data.util.DistanceCalculator
import javax.inject.Inject

class TaxiRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource
) : TaxiRepository {

    override suspend fun getCurrentLocation(): Pair<Double, Double>? {
        return locationDataSource.getCurrentLocation()
    }

    override fun getDriverLocations(): List<Driver> {
        return DriverDataSource.drivers
    }

    override suspend fun isAnyDriverWithin1Km(): Boolean {
        val (lat, lon) = locationDataSource.getCurrentLocation() ?: return false

        return DriverDataSource.drivers.any { driver ->
            val distance = DistanceCalculator.haversine(
                lat,
                lon,
                driver.latitude,
                driver.longitude
            )
            distance <= 1.0
        }
    }

    override suspend fun getNearestDriverWithin1Km(): Driver? {
        val (lat, lon) = locationDataSource.getCurrentLocation() ?: return null

        return DriverDataSource.drivers
            .map { driver ->
                val distance = DistanceCalculator.haversine(
                    lat,
                    lon,
                    driver.latitude,
                    driver.longitude
                )
                driver to distance
            }
            .filter { it.second <= 1.0 }
            .minByOrNull { it.second }
            ?.first
    }
}