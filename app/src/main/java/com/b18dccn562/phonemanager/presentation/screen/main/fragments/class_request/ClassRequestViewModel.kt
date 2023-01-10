package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_request

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.network.dto.ClassRequestDTO
import com.b18dccn562.phonemanager.network.repository.ClassRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassRequestViewModel @Inject constructor(
    private val classRepository: ClassRepository
) : ViewModel() {

    private val job = Job()

    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val _loadClassRequestStatus =
        MutableLiveData<Resource<BaseResponse<List<ClassRequestDTO>>>>()
    val loadClassRequestStatus: LiveData<Resource<BaseResponse<List<ClassRequestDTO>>>> =
        _loadClassRequestStatus

    fun loadClassRequest(classId: Long) {
        scope.launch {
            _loadClassRequestStatus.postValue(Resource.Loading())
            try {
                val result = classRepository.loadRequestForClass(classId)
                _loadClassRequestStatus.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _loadClassRequestStatus.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }

    }

    fun acceptRequest(requestId: Long) {
        scope.launch {
            _loadClassRequestStatus.postValue(Resource.Loading())
            try {
                val result = classRepository.acceptJoinClass(requestId)
                _loadClassRequestStatus.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _loadClassRequestStatus.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

    fun denyRequest(requestId: Long) {
        scope.launch {
            _loadClassRequestStatus.postValue(Resource.Loading())
            try {
                val result = classRepository.denyJoinClass(requestId)
                _loadClassRequestStatus.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _loadClassRequestStatus.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

}