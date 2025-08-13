package com.example.bookingappassignment.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun getStringFromThisResourceId(@StringRes intResId: Int, context: Context): String {
    return context.getString(intResId)
}

fun <T> AppCompatActivity.collectLatestLifeCycleFlow(
    flow: SharedFlow<T>,
    collect: suspend (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> AppCompatActivity.collectLatestLifeCycleFlow(
    flow: StateFlow<T>,
    collect: suspend (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun getCurrentTimeDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    return (sdf.format(Date()))
}

fun showDateTimePicker(
    context: Context,
    fragmentManager: androidx.fragment.app.FragmentManager,
    onDateTimeSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()

    val datePicker =
        MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .build()

    datePicker.addOnPositiveButtonClickListener { selection ->
        calendar.timeInMillis = selection


        val timePicker =
            MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                .setMinute(calendar.get(Calendar.MINUTE))
                .setTitleText("Select Time")
                .build()

        timePicker.addOnPositiveButtonClickListener {
            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour)
            calendar.set(Calendar.MINUTE, timePicker.minute)

            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            onDateTimeSelected(sdf.format(calendar.time))
        }

        timePicker.show(fragmentManager, "TIME_PICKER")
    }

    datePicker.show(fragmentManager, "DATE_PICKER")
}

