package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.local_database.room.local_model.DailyUsage
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.local_database.room.local_model.Setting
import com.b18dccn562.phonemanager.local_database.room.repository.AppRepository
import com.b18dccn562.phonemanager.network.dto.AppSettingDTO
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.network.repository.AppSettingRepository
import com.b18dccn562.phonemanager.network.repository.AppUsageRepository
import com.b18dccn562.phonemanager.utils.AppUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val appUtils: AppUtils,
    private val appRepository: AppRepository,
    private val appSettingRepository: AppSettingRepository,
    private val application: Application,
    private val appUsageRepository: AppUsageRepository
) : ViewModel() {

    private val scope = CoroutineScope(Dispatchers.IO + Job())

    val appSetting: LiveData<List<Setting>> = appRepository.allSetting

    private val _allAppFromDevice = MutableLiveData<Resource<List<ItemApp>>>()
    val allAppFromDevice: LiveData<Resource<List<ItemApp>>> = _allAppFromDevice

    private val _allAppFromDatabase = MutableLiveData<Resource<List<ItemApp>>>()

    private val _appLockedList = MutableLiveData<Resource<List<ItemApp>>>()
    val appLockList: LiveData<Resource<List<ItemApp>>> = _appLockedList

    private val _appLimitList = MutableLiveData<Resource<List<ItemApp>>>()
    val appLimitList: LiveData<Resource<List<ItemApp>>> = _appLimitList

    private val _isInitializing = MutableLiveData(false)
    val isInitializing: LiveData<Boolean> = _isInitializing

    private var _loadingAppFromDevice = true
    private var _loadingAppFromDatabase = true
    private var _loadingAppToDatabase = true

    fun loadAllApps() {
        scope.launch {
            _isInitializing.postValue(true)
            loadAppFromDevice()
            loadAppFromDatabase()
            checkAppUsage()
        }
    }

    private suspend fun checkAppUsage() {
        val allAppUsage = appRepository.getAllAppUsage()
        if (allAppUsage.isEmpty()) {
            val allAppFromDevice = appUtils.loadAllApp().distinctBy {
                it.packageName
            }
            for (app in allAppFromDevice) {
                val appUsage = DailyUsage(app.packageName, 0L)
                appRepository.insertAppUsage(appUsage)
            }
        }
    }

    private fun loadAppFromDevice() {
        _allAppFromDevice.postValue(Resource.Loading())
        val allAppFromDevice = appUtils.loadAllApp().distinctBy {
            it.packageName
        }
        if (allAppFromDevice.isEmpty()) {
            _allAppFromDevice.postValue(Resource.Empty())
        } else {
            _allAppFromDevice.postValue(Resource.Success(allAppFromDevice))
        }
        _loadingAppFromDevice = false
        checkStatus()
    }

    private suspend fun loadAppFromDatabase() {
        val allAppFromDatabase = appRepository.getAllApps()
        if (allAppFromDatabase.isEmpty()) {
            loadAppFromDeviceToDatabase()
        } else {
            withContext(Dispatchers.Main) {
                _allAppFromDatabase.value = Resource.Success(allAppFromDatabase)
            }
            _loadingAppFromDatabase = false
            _loadingAppToDatabase = false
            loadLockAndLimitApps()
//            checkServerApps()
        }
    }

    private suspend fun checkServerApps() {
        val data = appRepository.getAllApps().map {
            it.packageName
        }
        scope.launch {
            val call = async {
                appSettingRepository.uploadAppForCheckingDatabase(data)
            }
            val result = call.await()
            if (result.code == 200) {
                val missingApps = result.data!!
                for (app in missingApps) {
                    uploadImageToServer(app)
                }
            }
        }
    }

    private suspend fun loadAppFromDeviceToDatabase() {
        val allAppFromDevice = appUtils.loadAllApp().distinctBy {
            it.packageName
        }
        for (app in allAppFromDevice) {
            appRepository.addApp(app)
            val setting = Setting(
                isLock = false,
                isLimited = 0L,
                appPackageName = app.packageName
            )
            appRepository.addSetting(setting)
        }
        _loadingAppToDatabase = false
        loadAppFromDatabase()
    }

    private suspend fun loadLockAndLimitApps() {
        val appLockList = mutableListOf<ItemApp>()
        val appLimitList = mutableListOf<ItemApp>()
        val allAppFromDatabase = _allAppFromDatabase.value!!.data!!
        for (app in allAppFromDatabase) {
            val appSetting = appRepository.getAppSetting(app.packageName)[0]
            if (appSetting.setting.isLock) {
                appLockList.add(appSetting.app)
            }
            if (appSetting.setting.isLimited > 0L) {
                appLimitList.add(appSetting.app)
            }
        }
        if (appLockList.isEmpty()) {
            _appLockedList.postValue(Resource.Empty())
        } else {
            _appLockedList.postValue(Resource.Success(appLockList))
        }
        if (appLimitList.isEmpty()) {
            _appLimitList.postValue(Resource.Empty())
        } else {
            _appLimitList.postValue(Resource.Success(appLimitList))
        }
        checkStatus()
    }

    private fun checkStatus() {
        if (!_loadingAppFromDatabase && !_loadingAppFromDevice && !_loadingAppToDatabase) {
            _isInitializing.postValue(false)
        }
    }

    fun lockOrUnLockApp(app: ItemApp) {
        scope.launch {
            appRepository.lockOrUnLockApp(app)
        }
    }

    fun changeAppLimit(app: ItemApp, timeLimit: Long) {
        scope.launch {
            appRepository.changeAppLimit(app, timeLimit)
        }
    }

    fun appSettingReload() {
        scope.launch {
            loadAppFromDatabase()
        }
    }

    private suspend fun uploadImageToServer(appPackageName: String) {
        val app = appRepository.getApp(appPackageName)
        val appDrawable = appUtils.getAppIconByAppName(app.packageName)
        val iconBitmap: Bitmap
        appDrawable?.let {
            val originalBitmap = appUtils.convertDrawableToBitmap(it)
            iconBitmap = appUtils.resizeBitmap(
                originalBitmap, originalBitmap.width, originalBitmap.height
            )

            val f = File(application.cacheDir, app.packageName)
            f.createNewFile()

            val bos = ByteArrayOutputStream()
            iconBitmap.compress(Bitmap.CompressFormat.JPEG, 100 /*ignored for PNG*/, bos)
            val bitmapData: ByteArray = bos.toByteArray()

            var fos: FileOutputStream? = null
            try {
                fos = FileOutputStream(f)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            try {
                fos?.write(bitmapData)
                fos?.flush()
                fos?.close()
                val body = MultipartBody.Part.createFormData(
                    "image",
                    f.name,
                    f.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                )
                appUsageRepository.uploadAppImage(
                    app.packageName,
                    app.appName,
                    body
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun uploadAppSetting(user: UserDTO) {
        scope.launch {
            val allAppSetting = appRepository.getAllAppSetting()
            for (app in allAppSetting) {
                Log.d("OkHttp-------", app.appPackageName)
                val data = AppSettingDTO()
                data.app = appRepository.getApp(app.appPackageName)
                data.isLock = app.isLock
                data.isLimited = app.isLimited
                data.user = user
                appSettingRepository.uploadAppSetting(user.id, data)
            }
        }
    }

}