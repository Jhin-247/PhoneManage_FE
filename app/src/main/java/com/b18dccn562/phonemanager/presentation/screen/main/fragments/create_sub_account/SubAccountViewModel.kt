package com.b18dccn562.phonemanager.presentation.screen.main.fragments.create_sub_account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.local_database.shared_preference.AccountPreference
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.network.repository.AccountRepository
import com.b18dccn562.phonemanager.utils.signUpNewAccountVariables
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubAccountViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val accountPreference: AccountPreference
) : ViewModel() {
    private val job = Job()

    private val mainViewModelScope = CoroutineScope(Dispatchers.IO + job)
    val mFullName = MutableLiveData("")
    val mEmail = MutableLiveData("")
    val mPassword = MutableLiveData("")
    val mRePassword = MutableLiveData("")

    private val _createUserState = MutableLiveData<Resource<BaseResponse<UserDTO>>>()
    val createUserState: LiveData<Resource<BaseResponse<UserDTO>>> = _createUserState

    private var role = Constants.Role.CHILD

    fun signUp() {
        if (isConditionsFullFill()) {
            doSignUp(
                mEmail.value!!,
                mPassword.value!!,
                mFullName.value!!
            )
        }
    }

    private fun doSignUp(email: String, password: String, username: String) {
        mainViewModelScope.launch {
            _createUserState.postValue(Resource.Loading())
            try {
                val result = accountRepository.createSubAccount(
                    accountPreference.getEmail(),
                    email,
                    password,
                    username,
                    role
                )
                _createUserState.postValue(Resource.Success(result))
                signUpNewAccountVariables(email)
            } catch (exception: Exception) {
                _createUserState.postValue(Resource.Error("Something went wrong with ${exception.message}"))
            }
        }
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

}