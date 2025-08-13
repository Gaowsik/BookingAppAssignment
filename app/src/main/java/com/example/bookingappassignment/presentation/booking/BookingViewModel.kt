package com.example.bookingappassignment.presentation.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingappassignment.data.model.Driver
import com.example.bookingappassignment.data.repository.TaxiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(val taxiRepository: TaxiRepository) : ViewModel() {
    private val _nearestDriver = MutableStateFlow<Driver?>(null)
    val nearestDriver: StateFlow<Driver?> = _nearestDriver

    private val _bookingState = MutableStateFlow<BookingUiState>(BookingUiState())
    val bookingState = _bookingState.asStateFlow()

    private val _validateBookingState =
        MutableSharedFlow<ValidateBookingStates>()
    val validateBookingState = _validateBookingState.asSharedFlow()

    fun updateState(update: BookingUiState.() -> BookingUiState) {
        _bookingState.value = _bookingState.value.update()
    }


    fun getNearestDriver() {
        viewModelScope.launch {
            _nearestDriver.value = taxiRepository.getNearestDriverWithin1Km()
        }

    }

    fun validateEditScreen(
    ) {
        viewModelScope.launch {
            var formValidationState: ValidateBookingStates =
                ValidateBookingStates.STATE_VALID_FORM

            if (_bookingState.value.name.isBlank() || _bookingState.value.mobileNumber.isBlank() || _bookingState.value.tripTime.isBlank() || _bookingState.value.destination.isBlank() || _bookingState.value.paymentType.isBlank()) {
                formValidationState = ValidateBookingStates.STATE_EMPTY_FIELD
            } else if (_bookingState.value.mobileNumber.length != 10) {
                formValidationState = ValidateBookingStates.STATE_INVALID_MOBILE_NUMBER
            }


            _validateBookingState.emit(formValidationState)
        }
    }

}

data class BookingUiState(
    val name: String = "",
    val mobileNumber: String = "",
    val tripTime: String = "",
    val destination: String = "",
    val driverName: String = "",
    val paymentType: String = "",
    val bookingTime: String = ""
)

enum class ValidateBookingStates {
    STATE_EMPTY_FIELD, STATE_INVALID_MOBILE_NUMBER, STATE_VALID_FORM
}