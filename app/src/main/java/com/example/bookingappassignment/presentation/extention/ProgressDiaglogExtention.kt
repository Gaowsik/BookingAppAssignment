package com.example.bookingappassignment.presentation.extention

import android.app.Dialog
import android.content.Context
import com.example.bookingappassignment.R

fun Context.getProgressDialog(): Dialog {
    return Dialog(this).apply {
        this.setContentView(R.layout.dialog_progress)
        this.setCancelable(false)
        this.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }
}