package com.b18dccn562.phonemanager.presentation.screen.main.fragments.search_user.children

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
class SearchChildrenViewModel @Inject constructor(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val job = Job()

    private val scope = CoroutineScope(Dispatchers.IO + job)

    val searchText = MutableLiveData<String>()

    private val _searchStatus = MutableLiveData<Resource<BaseResponse<List<UserDTO>>>>()
    val searchStatus: LiveData<Resource<BaseResponse<List<UserDTO>>>> = _searchStatus

    private val _addKidRequest = MutableLiveData<Resource<BaseResponse<RequestDTO>>>()
    val addKidRequest: LiveData<Resource<BaseResponse<RequestDTO>>> = _addKidRequest

    private var user: UserDTO? = null

    private val _userList = MutableLiveData<List<UserDTO>?>()
    val userList: LiveData<List<UserDTO>?> = _userList

    fun search() {
        scope.launch {
            _searchStatus.postValue(Resource.Loading())
            try {
                val result = accountRepository.searchChildren(searchText.value ?: "", user!!.email)
                _searchStatus.postValue(Resource.Success(result))
            } catch (exception: Exception){
                _searchStatus.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

    fun setUser(user: UserDTO) {
        this.user = user
    }

    fun sendAddChildRequest(childEmail: String) {
        scope.launch {
            _addKidRequest.postValue(Resource.Loading())
            try {
                val result = accountRepository.addChildren(
                    user!!.email,
                    childEmail,
                    System.currentTimeMillis(),
                    Constants.RequestType.REQUEST_ADD_PARTNER
                )
                _addKidRequest.postValue(Resource.Success(result))
                pushPartnerRequestNotification(childEmail)
            } catch (exception: Exception) {
                _addKidRequest.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }

        }
    }

}