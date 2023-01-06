package com.b18dccn562.phonemanager.presentation.screen.main.fragments.notification_and_request.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.network.dto.NotificationDTO
import com.b18dccn562.phonemanager.network.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val job = Job()

    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val _loadNotificationState =
        MutableLiveData<Resource<BaseResponse<List<NotificationDTO>>>>()
    val loadNotificationState: LiveData<Resource<BaseResponse<List<NotificationDTO>>>> =
        _loadNotificationState

    fun getNotifications(userEmail: String) {
        scope.launch {
            _loadNotificationState.postValue(Resource.Loading())
            try {
                val result = notificationRepository.getNotifications(userEmail)
                _loadNotificationState.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _loadNotificationState.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }


}