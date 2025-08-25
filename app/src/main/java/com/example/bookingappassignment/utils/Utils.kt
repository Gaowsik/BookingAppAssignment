package com.example.bookingappassignment.utils

import android.content.Context
import android.graphics.Color
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.bookingappassignment.R
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog
import com.google.android.material.color.MaterialColors
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun getStringFromThisResourceId(@StringRes intResId: Int, context: Context): String {
    return context.getString(intResId)
}

fun <T> AppCompatActivity.collectLatestLifeCycleFlow(
    flow: SharedFlow<T>, collect: suspend (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collectLatest(collect)
        }
    }
}

fun <T> AppCompatActivity.collectLatestLifeCycleFlow(
    flow: StateFlow<T>, collect: suspend (T) -> Unit
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

fun showSingleDateTimePicker(
    context: Context, onDateTimeSelected: (String) -> Unit
) {
    SingleDateAndTimePickerDialog.Builder(context)
        .title(context.getString(R.string.title_select_date_time)).mainColor(
            MaterialColors.getColor(
                context, com.google.android.material.R.attr.colorPrimary, Color.BLUE
            )
        ).curved().minutesStep(1).listener { date ->
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            onDateTimeSelected(sdf.format(date))
        }.display()
}


