package com.b18dccn562.phonemanager.presentation.screen.main.fragments.search_user.parent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.network.dto.RequestDTO
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.network.repository.AccountRepository
import com.b18dccn562.phonemanager.utils.pushPartnerRequestNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPartnerViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val job = Job()

    private val scope = CoroutineScope(Dispatchers.IO + job)
    val searchText = MutableLiveData<String>()

    private var user: UserDTO? = null

    private val _searchStatus = MutableLiveData<Resource<BaseResponse<List<UserDTO>>>>()
    val searchStatus: LiveData<Resource<BaseResponse<List<UserDTO>>>> = _searchStatus

    private val _addPartnerRequest = MutableLiveData<Resource<BaseResponse<RequestDTO>>>()
    val addPartnerRequest: LiveData<Resource<BaseResponse<RequestDTO>>> = _addPartnerRequest

    fun search() {
        scope.launch {
            _searchStatus.postValue(Resource.Loading())
            try {
                val result = accountRepository.searchParent(searchText.value ?: "", user!!.email)
                _searchStatus.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _searchStatus.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

    fun setUser(user: UserDTO) {
        this.user = user
    }

    fun sendPartnerRequest(partnerEmail: String) {
        scope.launch {
            _addPartnerRequest.postValue(Resource.Loading())
            try {
                val result = accountRepository.addPartner(
                    user!!.email,
                    partnerEmail,
                    System.currentTimeMillis(),
                    Constants.RequestType.REQUEST_ADD_PARTNER
                )
                _addPartnerRequest.postValue(Resource.Success(result))
                pushPartnerRequestNotification(partnerEmail)
            } catch (exception: Exception) {
                _addPartnerRequest.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }

        }
    }

}