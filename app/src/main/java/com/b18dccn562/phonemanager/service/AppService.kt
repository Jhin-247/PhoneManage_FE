package com.b18dccn562.phonemanager.service

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Constants.NOTIFICATION_CHANNEL_ID
import com.b18dccn562.phonemanager.local_database.room.repository.AppRepository
import com.b18dccn562.phonemanager.local_database.shared_preference.AccountPreference
import com.b18dccn562.phonemanager.network.repository.AppUsageRepository
import com.b18dccn562.phonemanager.utils.AppUtils
import com.b18dccn562.phonemanager.utils.getRealtimeDatabase
import com.b18dccn562.phonemanager.utils.uploadLiveUsing
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.*
import javax.inject.Inject

@AndroidEntryPoint
class AppService : Service(), Observer {

    private val ioScope = CoroutineScope(Dispatchers.IO)

    private val detectScope = CoroutineScope(Dispatchers.IO)

    private var mOldRunningApp: String? = null
    private var mRunningApp: String? = null

    @Inject
    lateinit var appRepository: AppRepository

    @Inject
    lateinit var accountPreference: AccountPreference

    @Inject
    lateinit var appUtils: AppUtils

    @Inject
    lateinit var myLockWindowService: LockWindow

    @Inject
    lateinit var appUsageRepository: AppUsageRepository

    private lateinit var mLauncherName: String

    private var classId: Long = -1

    private var listener: ValueEventListener? = null

    private var trackForViolation: Boolean = false

    companion object {
        var mAppUnlocked: String? = "com.b18dccn562.phonemanager"
        var username: String = ""
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        ObserverForService.addObserver(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotification()
        initVariables()
        runDetect()
        return START_NOT_STICKY
    }

    private fun runDetect() {
        detectScope.launch {
            while (true) {
                mOldRunningApp = mRunningApp
                mRunningApp = appUtils.getRunningApp()?.packageName
                if (mOldRunningApp != mRunningApp && mOldRunningApp != null && mRunningApp != null) {
                    if (trackForViolation) {
                        checkAndUploadViolation(mRunningApp!!, mOldRunningApp!!)
                    } else {
                        checkAndUploadAppUsage(mRunningApp!!, mOldRunningApp!!)
                    }
                }
                if (mRunningApp == mLauncherName) {
                    CoroutineScope(Dispatchers.Main).launch {
                        myLockWindowService.closeScreenFromLauncher()
                    }
                    delay(300)
                    continue
                }
                detectAppLockAndLimit()
                delay(300)
            }
        }
    }

    private suspend fun checkAndUploadViolation(runningApp: String, oldApp: String) {
        val firebaseAppName = appUtils.getAppName(runningApp)
        if (runningApp == mLauncherName) {
            uploadAppUsageViolation(oldApp, Constants.AppActions.CLOSE_APP)
            uploadLiveUsing(classId, accountPreference.getEmail(), username, "", firebaseAppName)
        } else if (oldApp != mLauncherName) {
            uploadAppUsageViolation(oldApp, Constants.AppActions.CLOSE_APP)
            uploadAppUsageViolation(runningApp, Constants.AppActions.OPEN_APP)
            uploadLiveUsing(
                classId,
                accountPreference.getEmail(),
                username,
                mRunningApp!!,
                firebaseAppName
            )
        } else {
            uploadAppUsageViolation(runningApp, Constants.AppActions.OPEN_APP)
            uploadLiveUsing(
                classId,
                accountPreference.getEmail(),
                username,
                mRunningApp!!,
                firebaseAppName
            )
        }
    }

    private suspend fun uploadAppUsageViolation(appPackageName: String, appAction: Int) {
        val email = accountPreference.getEmail()
        if (email.isEmpty()) {
            return
        }
        val app = appRepository.getApp(appPackageName)
        try {
            val result = appUsageRepository.uploadAppUsageViolation(
                email,
                app.packageName,
                app.appName,
                System.currentTimeMillis(),
                appAction,
                classId
            )
            if (result.code == Constants.ResponseCode.NEED_IMAGE) {
                uploadImageToServer(appPackageName)
            }
        } catch (exception: Exception) {
            Log.e("AppService", "Error: ${exception.message}")
        }
    }

    private suspend fun checkAndUploadAppUsage(runningApp: String, oldApp: String) {
        if (runningApp == mLauncherName) {
            uploadAppUsage(oldApp, Constants.AppActions.CLOSE_APP)
        } else if (oldApp != mLauncherName) {
            uploadAppUsage(oldApp, Constants.AppActions.CLOSE_APP)
            uploadAppUsage(runningApp, Constants.AppActions.OPEN_APP)
        } else {
            uploadAppUsage(runningApp, Constants.AppActions.OPEN_APP)
        }
    }

    private suspend fun uploadAppUsage(appPackageName: String, appAction: Int) {
        val email = accountPreference.getEmail()
        if (email.isEmpty()) {
            return
        }
        val app = appRepository.getApp(appPackageName)
        try {
            val result = appUsageRepository.uploadAppUsage(
                email,
                app.packageName,
                app.appName,
                System.currentTimeMillis(),
                appAction
            )
            if (result.code == Constants.ResponseCode.NEED_IMAGE) {
                uploadImageToServer(appPackageName)
            }
        } catch (exception: Exception) {
            Log.e("AppService", "Error: ${exception.message}")
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

    private suspend fun detectAppLockAndLimit() {
        val allAppLimit = appRepository.getAllAppLimit().map {
            it.packageName
        }

        if (allAppLimit.contains(mRunningApp)) {
            if (checkIfPassLimit(mRunningApp!!)) {
                CoroutineScope(Dispatchers.Main).launch {
                    myLockWindowService.openLimitedScreen(mRunningApp!!)
                }
            } else {
                addTimeUsed(mRunningApp!!)
            }
        }

        val allAppLock = appRepository.getAllAppLock().map {
            it.packageName
        }
        if (allAppLock.contains(mRunningApp) && mAppUnlocked != mRunningApp) {
            CoroutineScope(Dispatchers.Main).launch {
                myLockWindowService.openLock(mRunningApp!!)
            }
        }
    }

    private suspend fun addTimeUsed(mRunningApp: String) {
        val usage = appRepository.getAppUsage(mRunningApp)
        usage.timeUsedInDay = usage.timeUsedInDay + 300
        appRepository.insertAppUsage(usage)
    }

    private suspend fun checkIfPassLimit(mRunningApp: String): Boolean {
        val timeUsed = appRepository.getAppUsageInDay(mRunningApp)
        val appLimitedTime = appRepository.getAppSetting(mRunningApp)[0].setting.isLimited
        return appLimitedTime <= timeUsed
    }

    private fun initVariables() {
        myLockWindowService.addViewFromBegin()
        ioScope.launch {
            mLauncherName = appUtils.getLauncherPackageName()
            Constants.launcherName = mLauncherName
        }
    }

    private fun createNotification() {
        val mNotification: Notification =
            NotificationCompat.Builder(application, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.running))
                .build()
        startForeground(1, mNotification)
    }

    override fun onDestroy() {
        super.onDestroy()
        ObserverForService.removeObserver(this)
    }

    override fun update() {
        classId = Constants.classId
        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    trackForViolation = true
                } else {
                    trackForViolation = false
                    Constants.classId = -1
                    removeClassListener()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        val ref = "Class"
        getRealtimeDatabase().getReference(ref).child(classId.toString()).child("is_started")
            .addValueEventListener(listener!!)
    }

    private fun removeClassListener() {
        val ref = "Class"
        listener?.let {
            getRealtimeDatabase().getReference(ref).child(classId.toString()).child("is_started")
                .removeEventListener(it)
            listener = null
        }
    }

}