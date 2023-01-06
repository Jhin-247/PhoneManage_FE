package com.b18dccn562.phonemanager.presentation.screen.main.fragments.sign_up

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    val mFullName = MutableLiveData("")
    val mEmail = MutableLiveData("")
    val mPassword = MutableLiveData("")
    val mRePassword = MutableLiveData("")

    private val _isReadyToSignup = MutableLiveData<Boolean>()
    val isReadyToSignup: LiveData<Boolean> = _isReadyToSignup

    fun getFullName(): String {
        return mFullName.value!!
    }

    fun getEmail(): String {
        return mEmail.value!!
    }

    fun getPassword(): String {
        return mPassword.value!!
    }

    private var role = -1

    fun signUp() {
        _isReadyToSignup.value = isConditionsFullFill()
    }

    private fun isConditionsFullFill(): Boolean {
        return variablesNotNull() && passwordIsTheSame()
    }

    private fun passwordIsTheSame(): Boolean {
        val password = mPassword.value
        val rePassword = mPassword.value
        return password == rePassword
    }

    private fun variablesNotNull(): Boolean {
        val fullName = mFullName.value
        if (fullName == null || fullName.isEmpty()) {
            return false
        }

        val email = mEmail.value
        if (email == null || email.isEmpty()) {
            return false
        }

        val password = mPassword.value
        if (password == null || password.isEmpty()) {
            return false
        }

        val rePassword = mRePassword.value
        if (rePassword == null || rePassword.isEmpty()) {
            return false
        }

        return true
    }

    fun setRole(userRole: Int) {
        role = userRole
    }

}