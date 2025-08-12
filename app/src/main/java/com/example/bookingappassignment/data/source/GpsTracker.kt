package com.example.bookingappassignment.data.source

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class GpsTracker(private val context: Context) {

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(): Pair<Double, Double>? = suspendCancellableCoroutine { cont ->
        val cancellationTokenSource = CancellationTokenSource()

        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                cont.resume(Pair(location.latitude, location.longitude))
            } else {
                // Fallback to last known location
                getLastKnownLocation(cont)
            }
        }.addOnFailureListener { e ->
            Log.e("GpsTracker", "Failed to get current location: ${e.message}")
            getLastKnownLocation(cont)
        }

        cont.invokeOnCancellation {
            cancellationTokenSource.cancel()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastKnownLocation(cont: kotlinx.coroutines.CancellableContinuation<Pair<Double, Double>?>) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                cont.resume(location?.let { Pair(it.latitude, it.longitude) })
            }
            .addOnFailureListener { e ->
                Log.e("GpsTracker", "Failed to get last known location: ${e.message}")
                cont.resume(null)
            }
    }
}