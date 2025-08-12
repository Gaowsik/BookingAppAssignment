package com.example.bookingappassignment.data.repository

import android.location.Location
import com.example.bookingappassignment.data.model.Driver

interface TaxiRepository {

    suspend fun getCurrentLocation():  Pair<Double, Double>?

    fun getDriverLocations(): List<Driver>

    suspend fun isAnyDriverWithin1Km(): Boolean

    suspend fun getNearestDriverWithin1Km(): Driver?
}