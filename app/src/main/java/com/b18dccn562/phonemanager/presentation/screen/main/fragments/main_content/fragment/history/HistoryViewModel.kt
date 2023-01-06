package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.utils.AppUtils
import com.b18dccn562.phonemanager.utils.enum_data.AppQueryEnum
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val appUtils: AppUtils
) : ViewModel() {

    private val _listAppToShow = MutableLiveData<List<ItemApp>>()
    val listAppToShow: LiveData<List<ItemApp>> = _listAppToShow

    fun query(queryType: AppQueryEnum) {
        CoroutineScope(Dispatchers.IO).launch {
            val listApp = appUtils.queryAppsUsageFromDevices(queryType)
            _listAppToShow.postValue(listApp)
        }

    }

}