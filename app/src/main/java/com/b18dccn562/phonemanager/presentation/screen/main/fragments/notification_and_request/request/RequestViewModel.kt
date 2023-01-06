package com.b18dccn562.phonemanager.presentation.screen.main.fragments.notification_and_request.request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.network.dto.RequestDTO
import com.b18dccn562.phonemanager.network.repository.RequestRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val requestRepository: RequestRepository
) : ViewModel() {
    private val job = Job()

    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val _loadRequestState =
        MutableLiveData<Resource<BaseResponse<List<RequestDTO>>>>()
    val loadRequestState: LiveData<Resource<BaseResponse<List<RequestDTO>>>> =
        _loadRequestState

//    private val _processRequest = MutableLiveData<Resource<BaseResponse<List<RequestDTO>>>>()
//    val processRequest: LiveData<Resource<BaseResponse<List<RequestDTO>>>> = _processRequest

    fun loadRequests(userEmail: String) {
        scope.launch {
            _loadRequestState.postValue(Resource.Loading())
            try {
                val result = requestRepository.getRequest(userEmail)
                _loadRequestState.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _loadRequestState.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

    fun acceptRequest(requestDTO: RequestDTO) {
        scope.launch {
            _loadRequestState.postValue(Resource.Loading())
            try {
                val result = requestRepository.acceptRequest(requestDTO)
                _loadRequestState.postValue(Resource.Success(result))
            } catch (exception: Exception){
                _loadRequestState.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

    fun denyRequest(requestDTO: RequestDTO) {
        scope.launch {
            _loadRequestState.postValue(Resource.Loading())
            try {
                val result = requestRepository.denyRequest(requestDTO)
                _loadRequestState.postValue(Resource.Success(result))
            } catch (exception: Exception){
                _loadRequestState.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

}