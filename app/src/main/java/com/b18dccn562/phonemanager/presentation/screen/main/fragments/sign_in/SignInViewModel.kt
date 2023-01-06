package com.b18dccn562.phonemanager.presentation.screen.main.fragments.sign_in

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor() : ViewModel() {

    val mEmail = MutableLiveData("")
    val mPassword = MutableLiveData("")

    private val _isReadyToLogin = MutableLiveData<Boolean>()
    val isReadyToLogin: LiveData<Boolean> = _isReadyToLogin

    fun login() {
        _isReadyToLogin.value = isConditionsFullFill()
    }

    fun getEmail(): String {
        return mEmail.value!!
    }

    fun getPassword(): String {
        return mPassword.value!!
    }

    private fun isConditionsFullFill(): Boolean {
        val email = mEmail.value
        if (email == null || email.isEmpty()) {
            return false
        }

        val password = mPassword.value
        if (password == null || password.isEmpty()) {
            return false
        }
        return true
    }

}