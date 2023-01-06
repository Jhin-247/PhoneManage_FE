package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_report

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.network.dto.AppUsageDTO
import com.b18dccn562.phonemanager.network.dto.ReportDTO
import com.b18dccn562.phonemanager.network.repository.AppUsageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassReportViewModel @Inject constructor(
    private val appUsageRepository: AppUsageRepository
): ViewModel(){
    private val job = Job()

    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val _loadAppUsageStatus = MutableLiveData<Resource<BaseResponse<List<ReportDTO>>>>()
    val loadAppUsageStatus: LiveData<Resource<BaseResponse<List<ReportDTO>>>> =
        _loadAppUsageStatus

    fun loadAppUsage(classId: Long) {
        scope.launch {
            _loadAppUsageStatus.postValue(Resource.Loading())
            try {
                val result =
                    appUsageRepository.getClassReport(classId)
                _loadAppUsageStatus.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _loadAppUsageStatus.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }
}