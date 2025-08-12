package com.example.bookingappassignment.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingappassignment.data.model.Driver
import com.example.bookingappassignment.data.repository.TaxiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val taxiRepository : TaxiRepository ) : ViewModel() {


    private val _currentLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val currentLocation: StateFlow<Pair<Double, Double>?> = _currentLocation

    private val _driverLocations = MutableStateFlow<List<Driver>>(emptyList())
    val driverLocations: StateFlow<List<Driver>> = _driverLocations

    private val _isDriverNearby = MutableStateFlow(false)
    val isDriverNearby: StateFlow<Boolean> = _isDriverNearby

    fun loadMapData() {
        viewModelScope.launch {
            _currentLocation.value = taxiRepository.getCurrentLocation()
            _driverLocations.value = taxiRepository.getDriverLocations()
            _isDriverNearby.value = taxiRepository.isAnyDriverWithin1Km()
        }
    }

    suspend fun getNearestDriver() = taxiRepository.getNearestDriverWithin1Km()

}