package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_report

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.network.dto.ReportDTO
import com.b18dccn562.phonemanager.network.repository.AppUsageRepository
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.user_report.getAppIcon
import com.b18dccn562.phonemanager.utils.getRealtimeDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassReportViewModel @Inject constructor(
    private val appUsageRepository: AppUsageRepository
) : ViewModel() {
    private val job = Job()

    val isLive = MutableLiveData<Boolean>()
    private var listener: ValueEventListener? = null

    private val scope = CoroutineScope(Dispatchers.IO + job)
    private val _loadAppUsageStatus = MutableLiveData<Resource<BaseResponse<List<ReportDTO>>>>()
    val loadAppUsageStatus: LiveData<Resource<BaseResponse<List<ReportDTO>>>> =
        _loadAppUsageStatus

    private val _liveStudentList = mutableListOf<LiveStudentReport>()

    private val _liveStudentReportList = MutableLiveData<List<LiveStudentReport>>()
    val liveStudentReportList: LiveData<List<LiveStudentReport>> = _liveStudentReportList

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

    fun observeLiveData(classId: Long) {
        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val liveStudentReport = data.getValue(LiveStudentReport::class.java)
                    _liveStudentList.clear()
                    liveStudentReport?.let {
                        if (it.using.isNotEmpty()) {
                            val liveData = LiveStudentReport()
                            liveData.name = it.name.replace("_",".")
                            liveData.using = getAppIcon(it.using.replace("_","."))
                            liveData.app_name = it.app_name
                            _liveStudentList.add(liveData)
                        }
                    }
                    _liveStudentReportList.postValue(_liveStudentList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        getRealtimeDatabase().getReference("Class").child(classId.toString()).child("Student")
            .addValueEventListener(listener!!)
    }

    fun removeOldListener(classId: Long) {
        listener?.let {
            getRealtimeDatabase().getReference("Class").child(classId.toString()).child("Student")
                .removeEventListener(it)
            listener = null
        }
    }
}