package com.example.bookingappassignment.presentation.booking

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookingappassignment.R
import com.example.bookingappassignment.databinding.ActivityBookingBinding
import com.example.bookingappassignment.databinding.ActivityMainBinding
import com.example.bookingappassignment.presentation.core.BaseActivity
import com.example.bookingappassignment.presentation.main.MainViewModel
import com.example.bookingappassignment.utils.collectLatestLifeCycleFlow
import com.example.bookingappassignment.utils.getCurrentTimeDate
import com.example.bookingappassignment.utils.showSingleDateTimePicker
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class BookingActivity : BaseActivity() {
    private lateinit var binding: ActivityBookingBinding
    private val viewModel: BookingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        init()
    }

    private fun init() {
        bindUI()
        setUpDropDown()
        getNearestDriver()
        setUpObservers()
        setBookingTime()
        setUpListeners()

    }

    private fun bindUI() {
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.etTripTime.isFocusable = false
        binding.etTripTime.isClickable = false
    }


    private fun setUpObservers() {

        this.collectLatestLifeCycleFlow(viewModel.bookingState) { bookingState ->
            renderState(bookingState)

        }

        this.collectLatestLifeCycleFlow(viewModel.validateBookingState) { validateBookingState ->

            handleValidateBookingState(validateBookingState)
        }


        this.collectLatestLifeCycleFlow(viewModel.nearestDriver) { nearestDriver ->
            if (nearestDriver != null) {
                viewModel.updateState { copy(driverName = nearestDriver.name) }
            }

        }

    }

    private fun setUpDropDown() {
        val paymentOptions = listOf("Card", "Cash")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, paymentOptions)
        binding.etPaymentType.setAdapter(adapter)
    }

    private fun getNearestDriver() {
        viewModel.getNearestDriver()
    }

    private fun setBookingTime() {
        viewModel.updateState { copy(bookingTime = getCurrentTimeDate()) }
        binding.etBookingTime.isFocusable = false
        binding.etBookingTime.isClickable = false
    }

    private fun setUpListeners() {
        binding.etTripTime.setOnClickListener {
            showSingleDateTimePicker(this) { formattedDateTime ->
                binding.etTripTime.setText(formattedDateTime)
            }
        }

        binding.btnBook.setOnClickListener {
            viewModel.updateState {
                copy(
                    paymentType = binding.etPaymentType.text.toString(),
                    tripTime = binding.etTripTime.text.toString(),
                    name = binding.etName.text.toString(),
                    mobileNumber = binding.etMobileNumber.text.toString(),
                    destination = binding.etDestination.text.toString()
                )
            }

            validateBookingScreen()
        }

        binding.etPaymentType.setOnItemClickListener { parent, view, position, id ->
            val selectedValue = parent.getItemAtPosition(position).toString()
            binding.etPaymentType.setText(selectedValue)
        }


    }

    private fun renderState(state: BookingUiState) {
        binding.etName.setText(state.name)
        binding.etMobileNumber.setText(state.mobileNumber)
        binding.etTripTime.setText(state.tripTime)
        binding.etDestination.setText(state.destination)
        binding.etDrivarsName.setText(state.driverName)
        binding.etPaymentType.setText(state.paymentType)
        binding.etBookingTime.setText(state.bookingTime)
    }

    private fun validateBookingScreen() {
        viewModel.validateEditScreen()
    }

    private fun handleValidateBookingState(validateBookingStates: ValidateBookingStates?) {
        var validationMessageString: String? = null

        when (validateBookingStates) {
            ValidateBookingStates.STATE_EMPTY_FIELD -> {
                validationMessageString =
                    getString(R.string.msg_empty_field)
            }

            ValidateBookingStates.STATE_INVALID_MOBILE_NUMBER -> {
                validationMessageString =
                    getString(R.string.msg_invalid_mobile_number)
            }

            ValidateBookingStates.STATE_VALID_FORM -> {
                validationMessageString =
                    getString(R.string.msg_success)

            }

            null -> TODO()
        }

        validationMessageString.let {
            showMessageDialogWithOkAction(messageContent = validationMessageString)
        }
    }
}
