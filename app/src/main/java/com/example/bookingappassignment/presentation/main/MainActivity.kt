package com.example.bookingappassignment.presentation.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bookingappassignment.R
import com.example.bookingappassignment.data.model.Driver
import com.example.bookingappassignment.databinding.ActivityMainBinding
import com.example.bookingappassignment.presentation.booking.BookingActivity
import com.example.bookingappassignment.presentation.core.BaseActivity
import com.example.bookingappassignment.utils.collectLatestLifeCycleFlow
import com.example.bookingappassignment.utils.permission.IGetPermissionListener
import com.example.bookingappassignment.utils.permission.PermissionUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity(), IGetPermissionListener, OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var requestLauncher: ActivityResultLauncher<String>
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val viewModel: MainViewModel by viewModels()
    private var googleMap: GoogleMap? = null

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
        setUpMapFragment()
        checkLocationPermission()
        setUpListener()
        setUpObservers()
        loadMapData()
    }

    private fun bindUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setUpMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    private fun setUpObservers() {
        this.collectLatestLifeCycleFlow(viewModel.currentLocation) { location ->
            googleMap?.clear() // Clear old markers
            if (location != null) {
                addUserMarker(location.first, location.second)
            }

            viewModel.driverLocations.value.let {
                addDriverMarkers(it)
            }

        }

        this.collectLatestLifeCycleFlow(viewModel.driverLocations) { drivers ->
            googleMap?.clear()
            viewModel.currentLocation.value?.let {
                addUserMarker(it.first, it.second)
            }
            addDriverMarkers(drivers)
        }

        this.collectLatestLifeCycleFlow(viewModel.isDriverNearby) {
            binding.bookNowButton.isEnabled = it
        }
    }

    private fun setUpListener() {
        binding.bookNowButton.setOnClickListener {
            navigateToBookingScreen()
        }
    }

    private fun navigateToBookingScreen() {
        val intent = Intent(this, BookingActivity::class.java)
        startActivity(intent)
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
            )
        )
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.uiSettings?.isZoomControlsEnabled = true
    }


    private fun loadMapData() {
        viewModel.loadMapData()
    }

    private fun addUserMarker(lat: Double, lng: Double) {
        val userLatLng = LatLng(lat, lng)
        googleMap?.addMarker(
            MarkerOptions()
                .position(userLatLng)
                .title(getString(R.string.msg_you_are_here))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
        )
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f))
    }

    private fun addDriverMarkers(drivers: List<Driver>) {
        drivers.forEach { driver ->
            val driverLatLng = LatLng(driver.latitude, driver.longitude)
            googleMap?.addMarker(
                MarkerOptions()
                    .position(driverLatLng)
                    .title(getString(R.string.msg_driver, driver.name))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
        }

    }
}