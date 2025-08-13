package com.example.bookingappassignment.presentation.booking

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
class BookingViewModel @Inject constructor(val taxiRepository: TaxiRepository) : ViewModel() {
    private val _nearestDriver = MutableStateFlow<Driver?>(null)
    val nearestDriver: StateFlow<Driver?> = _nearestDriver

    fun getNearestDriver() {
        viewModelScope.launch {
            _nearestDriver.value = taxiRepository.getNearestDriverWithin1Km()
        }
    }

}