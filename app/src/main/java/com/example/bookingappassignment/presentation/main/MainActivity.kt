package com.example.bookingappassignment.presentation.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookingappassignment.R
import com.example.bookingappassignment.databinding.ActivityMainBinding
import com.example.bookingappassignment.presentation.core.BaseActivity
import com.example.bookingappassignment.utils.permission.IGetPermissionListener
import com.example.bookingappassignment.utils.permission.PermissionUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), IGetPermissionListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestLauncher: ActivityResultLauncher<String>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    @Inject
    lateinit var permissionUtil: PermissionUtil

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            permissionUtil.handleSinglePermissionResult(this, isGranted)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        init()
    }


    private fun init() {
        bindUI()
        permissionUtil.setPermissionListener(this)
        requestLauncher = setRequestLauncher()
        resultLauncher = setResultLauncher()
        checkLocationPermission()
    }

    private fun bindUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    override fun onPermissionGranted() {
    }

    override fun onPermissionDenied() {
        checkLocationPermission()
    }

    override fun onPermissionRationale() {
        permissionAlertDialog()
    }


    private fun checkLocationPermission() {
        val locationPermission = android.Manifest.permission.ACCESS_FINE_LOCATION
        if (!permissionUtil.hasPermission(this, locationPermission)) {
            permissionUtil.requestPermission(locationPermission, locationPermissionLauncher)
        }
    }

    private fun setRequestLauncher() = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    )
    { isGranted: Boolean ->
        permissionUtil.handleSinglePermissionResult(
            activity = this,
            isGranted
        )
    }

    private fun setResultLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                checkLocationPermission()
            }

        }

    private fun permissionAlertDialog() {
        showMessageDialogWithOkAction(
            messageContent = getString(
                R.string.msg_permission_location
            ), okAction = {
                permissionUtil.openAppSettingPage(this, resultLauncher)
            }, okActionText = getString(
                R.string.action_setting,
            ))
    }

}