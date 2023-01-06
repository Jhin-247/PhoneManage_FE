package com.b18dccn562.phonemanager.service

import android.app.Application
import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.databinding.ScreenLockRemoteBinding
import com.b18dccn562.phonemanager.local_database.room.repository.AppRepository
import com.b18dccn562.phonemanager.local_database.shared_preference.AccountPreference
import com.b18dccn562.phonemanager.presentation.custom_view.lock_view.LockView
import com.b18dccn562.phonemanager.utils.AppUtils
import com.b18dccn562.phonemanager.utils.PatternUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LockWindow @Inject constructor(
    val application: Application,
    patternUtil: PatternUtil,
//    private val appService: AppService,
    private val appUtils: AppUtils,
    private val appRepository: AppRepository,
    private val accountUtils: AccountPreference
) {
    private val mView: ScreenLockRemoteBinding
    private var mParams: WindowManager.LayoutParams? = null
    private val mWindowManager: WindowManager
    private val layoutInflater: LayoutInflater
    private var windowManager: WindowManager? = null

    private var mAppLockOpening: String = ""

    init {
        windowManager = application.getSystemService(Service.WINDOW_SERVICE) as WindowManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.RGB_888
            )
        } else {
            @Suppress("DEPRECATION")
            mParams = WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.RGB_888
            )
        }
        layoutInflater =
            application.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = ScreenLockRemoteBinding.bind(
            layoutInflater.inflate(
                R.layout.screen_lock_remote,
                null
            )
        )
        mParams!!.gravity = Gravity.CENTER
        mWindowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager


        mView.lockView.setOnCompleteListener(object : LockView.OnCompleteDrawingListener {
            override fun onCompleteDrawing(patterns: List<Int>) {
                if (patternUtil.checkPattern(patterns)) {
                    closeScreen(mAppLockOpening)
                }
                mView.lockView.clearPattern()
            }
        })
    }

    fun openLock(appLocked: String) {
        mView.container.visibility = View.VISIBLE
        mView.cslLock.visibility = View.VISIBLE
        mView.cslLimited.visibility = View.GONE
        windowManager?.updateViewLayout(mView.root, mParams)
        mAppLockOpening = appLocked
    }

    fun openLimitedScreen(appLimited: String) {
        mView.container.visibility = View.VISIBLE
        mView.cslLock.visibility = View.GONE
        mView.cslLimited.visibility = View.VISIBLE
        windowManager?.updateViewLayout(mView.root, mParams)
        mAppLockOpening = appLimited
    }

    fun closeScreen(appOpened: String) {
        AppService.mAppUnlocked = appOpened
        mView.container.visibility = View.GONE
        windowManager?.updateViewLayout(mView.root, mParams)
    }

    fun closeScreenFromLauncher(){
        AppService.mAppUnlocked = ""
        mView.container.visibility = View.GONE
        windowManager?.updateViewLayout(mView.root, mParams)
    }

    fun addViewFromBegin() {
        try {
            if (mView.root.windowToken == null) {
                if (mView.root.parent == null) {
                    mView.container.visibility = View.GONE
                    mWindowManager.addView(mView.root, mParams)
                }
            }
        } catch (e: Exception) {
        }
    }
}