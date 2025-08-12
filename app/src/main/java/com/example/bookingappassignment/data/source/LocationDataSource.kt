package com.example.bookingappassignment.data.source

interface LocationDataSource {
    suspend fun getCurrentLocation(): Pair<Double, Double>?
}