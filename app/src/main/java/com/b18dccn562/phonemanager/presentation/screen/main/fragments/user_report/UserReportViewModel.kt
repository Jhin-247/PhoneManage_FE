package com.b18dccn562.phonemanager.presentation.screen.main.fragments.user_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.network.dto.AppUsageDTO
import com.b18dccn562.phonemanager.network.repository.AppUsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserReportViewModel @Inject constructor(
    private val appUsageRepository: AppUsageRepository
) : ViewModel() {
    private val job = Job()

    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val _loadAppUsageStatus = MutableLiveData<Resource<BaseResponse<List<AppUsageDTO>>>>()
    val loadAppUsageStatus: LiveData<Resource<BaseResponse<List<AppUsageDTO>>>> =
        _loadAppUsageStatus

    fun loadAppUsage(userEmail: String) {
        scope.launch {
            _loadAppUsageStatus.postValue(Resource.Loading())
            try {
                val result =
                    appUsageRepository.getUserAppUsage(userEmail, Constants.AppUsageQueryType.ALL)
                _loadAppUsageStatus.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _loadAppUsageStatus.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

}