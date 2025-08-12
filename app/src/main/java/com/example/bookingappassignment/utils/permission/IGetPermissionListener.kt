package com.example.bookingappassignment.utils.permission

interface IGetPermissionListener {
    fun onPermissionGranted()
    fun onPermissionDenied()
    fun onPermissionRationale()
}