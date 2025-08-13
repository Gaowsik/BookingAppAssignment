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
import com.example.bookingappassignment.presentation.main.MainViewModel
import com.example.bookingappassignment.utils.collectLatestLifeCycleFlow
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookingActivity : AppCompatActivity() {
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

    }

    private fun bindUI() {
        binding = ActivityBookingBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun setUpObservers() {
        this.collectLatestLifeCycleFlow(viewModel.nearestDriver) { driver ->
            if (driver != null) {
                binding.etDrivarsName.setText(driver.name)
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
}
