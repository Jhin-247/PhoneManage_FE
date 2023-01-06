package com.b18dccn562.phonemanager.presentation.screen.main.fragments.permission

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(

) : ViewModel() {
    private val _hasUsagePermission = MutableLiveData(false)
    val hasUsagePermission: LiveData<Boolean> = _hasUsagePermission

    private val _hasOverlayPermission = MutableLiveData(false)
    val hasOverlayPermission: LiveData<Boolean> = _hasOverlayPermission

    private val _hasAllPermission = MutableLiveData(0)
    val hasAllPermission: LiveData<Int> = _hasAllPermission

    fun setHasOverlayPermission(permission: Boolean) {
        _hasOverlayPermission.value = permission
        if (permission) {
            val numberOfPermissionGranted = _hasAllPermission.value!!
            _hasAllPermission.value = numberOfPermissionGranted + 1
        }
    }

    fun setHasUsagePermission(permission: Boolean) {
        _hasUsagePermission.value = permission
        if (permission) {
            val numberOfPermissionGranted = _hasAllPermission.value!!
            _hasAllPermission.value = numberOfPermissionGranted + 1
        }
    }


}