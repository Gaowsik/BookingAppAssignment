package com.example.bookingappassignment.presentation.extention

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bookingappassignment.R

private fun Activity.alert(
    title: CharSequence? = null,
    message: CharSequence? = null,
    isCancellable: Boolean = true,
    func: AlertDialogHelper.() -> Unit
): AlertDialog {
    return AlertDialogHelper(this, title, message).apply {
        cancelable = isCancellable
        func()
    }.create().also {
        it
    }
}


/**
 * show alert with less code base
 */
fun AppCompatActivity.showAlert(
    message: String,
    isCancellable: Boolean = true,
    title: String = getString(R.string.title_alert),
    okActionText: String = getString(R.string.action_ok),
    okAction: (() -> Unit)? = null,
    negativeAction: (() -> Unit)? = null,
    negativeActionText: String? = null
): AlertDialog {
    return alert(title, message) {
        cancelable = isCancellable
        positiveButton(okActionText) {
            okAction?.invoke()
        }
        negativeAction?.let {
            negativeButton(negativeActionText ?: getString(R.string.action_cancel)) {
                negativeAction.invoke()
            }
        }
    }.also {
        if (it.isShowing)
            it.dismiss()
        it.show()
    }
}