package com.example.bookingappassignment.presentation.core

import android.app.Dialog
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bookingappassignment.R
import com.example.bookingappassignment.presentation.extention.getProgressDialog
import com.example.bookingappassignment.presentation.extention.showAlert
import com.example.bookingappassignment.utils.getStringFromThisResourceId
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    var mCurrentShowingAlert: AlertDialog? = null
    private var mProgressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        init()
    }

    private fun init() {
        initProgressDialog()
    }


    private fun initProgressDialog() {
        mProgressDialog = getProgressDialog()
    }

    fun hideProgressDialog() {
        mProgressDialog?.dismiss()
    }

    fun showProgressDialog() {
        if (mProgressDialog?.isShowing == true)
            return
        initProgressDialog()
        mProgressDialog?.show()
    }

    fun showMessageDialogWithOkAction(
        messageContent: String?,
        okAction: (() -> Unit)? = null,
        isCancellable: Boolean = false,
        okActionText: String = getStringFromThisResourceId(R.string.action_ok, this),
    ) {
        configureAlertShowing(
            messageContent = messageContent,
            okAction = okAction,
            okActionText = okActionText,
            isCancellable = isCancellable,
            negativeAction = null,
            negativeActionText = null,
        )
    }

    fun showMessageDialogWithPositiveAndNegativeAction(
        messageContent: String?,
        okAction: (() -> Unit)? = null,
        okActionText: String,
        isCancellable: Boolean = false,
        negativeAction: (() -> Unit)? = null,
        negativeText: String?,
    ) {
        configureAlertShowing(
            messageContent = messageContent,
            okAction = okAction,
            okActionText = okActionText,
            isCancellable = isCancellable,
            negativeAction = negativeAction,
            negativeActionText = negativeText,
        )
    }

    private fun configureAlertShowing(
        messageContent: String?,
        okAction: (() -> Unit)?,
        okActionText: String,
        isCancellable: Boolean,
        negativeAction: (() -> Unit)?,
        negativeActionText: String?,
    ) {
        messageContent?.let {
            mCurrentShowingAlert = showAlert(
                message = messageContent,
                okAction = okAction,
                okActionText = okActionText,
                isCancellable = isCancellable,
                negativeAction = negativeAction,
                negativeActionText = negativeActionText,
            )
        }
    }
}