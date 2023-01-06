package com.b18dccn562.phonemanager.utils

import android.app.Application
import android.app.usage.UsageEvents
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.content.pm.PackageManager.NameNotFoundException
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.ArrayMap
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.graphics.drawable.toBitmap
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.utils.enum_data.AppQueryEnum
import com.b18dccn562.phonemanager.utils.enum_data.TimeUtils
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppUtils @Inject constructor(
    private val application: Application
) {

    private var packageManager: PackageManager = application.packageManager

    fun getAppIconByAppName(packageName: String): Drawable? {
        try {
            return packageManager.getApplicationIcon(packageName)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return AppCompatResources.getDrawable(
            application,
            R.mipmap.ic_launcher
        )
    }

    fun getAppName(packageName: String): String {
        var applicationInfo: ApplicationInfo? = null
        try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        } catch (e: NameNotFoundException) {
            e.printStackTrace()
        }
        return (
                (if (applicationInfo != null)
                    packageManager.getApplicationLabel(applicationInfo) else "Unknown") as String
                )
    }

    private fun isSystemApp(packageName: String): Boolean {
        val applicationInfo: ApplicationInfo?
        return try {
            applicationInfo = packageManager.getApplicationInfo(packageName, 0)
            packageManager.getLaunchIntentForPackage(applicationInfo.packageName) == null
        } catch (exception: Exception) {
            false
        }
    }

    fun queryAppsUsageFromDevices(type: AppQueryEnum): List<ItemApp> {
        val mAppInfoList: MutableList<ItemApp> = ArrayList()
        val map: ArrayMap<String, UsageStats> = ArrayMap()

        val beginTime: Long
        val queryType: Int

        when (type) {
            AppQueryEnum.ONE_DAY -> {
                beginTime = TimeUtils.getOneDayBeforeMilliSeconds()
                queryType = UsageStatsManager.INTERVAL_DAILY
            }
            AppQueryEnum.SEVEN_DAY -> {
                beginTime = TimeUtils.getSevenDayBeforeMilliSecond()
                queryType = UsageStatsManager.INTERVAL_WEEKLY
            }
            else -> {
                beginTime = TimeUtils.getThirdTyDayBeforeMilliSecond()
                queryType = UsageStatsManager.INTERVAL_MONTHLY
            }
        }

        val mUsageStats =
            (application.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager)
                .queryUsageStats(
                    queryType,
                    beginTime,
                    System.currentTimeMillis()
                )
        mUsageStats.forEach { usageStats ->
            if (isAppInfoAvailable(usageStats)) {
                val mExistingStat: UsageStats? = map[usageStats.packageName]
                map[usageStats.packageName] = usageStats
                mExistingStat?.add(usageStats)
            }
        }

        mUsageStats.clear()
        mUsageStats.addAll(map.values)

        for (usageStats in mUsageStats) {
            if (isSystemApp(usageStats.packageName)) {
                continue
            }
            val mAppInfo = ItemApp()
            mAppInfo.packageName = usageStats.packageName
            mAppInfo.appName = getAppName(usageStats.packageName)
            mAppInfo.appIcon = getAppIconByAppName(mAppInfo.packageName)
            mAppInfo.timeUsedInDay = usageStats.totalTimeInForeground
            if (mAppInfo.packageName != mAppInfo.appName && mAppInfo.timeUsedInDay != 0L)
                mAppInfoList.add(mAppInfo)
        }
        mAppInfoList.sortWith { o1: ItemApp, o2: ItemApp ->
            o2.timeUsedInDay.compareTo(o1.timeUsedInDay)
        }
        return mAppInfoList
    }

    private fun isAppInfoAvailable(usageStats: UsageStats): Boolean {
        return try {
            packageManager.getApplicationInfo(usageStats.packageName, 0)
            true
        } catch (e: NameNotFoundException) {
            false
        }
    }

    fun loadAllApp(): List<ItemApp> {
        val i = Intent(Intent.ACTION_MAIN)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val lst: List<ResolveInfo> = packageManager.queryIntentActivities(i, 0)
        val result: MutableList<ItemApp> = ArrayList()
        for (resolveInfo in lst) {
            result.add(convertResolveInfo(resolveInfo))
        }

        result.sortWith { o1: ItemApp, o2: ItemApp ->
            o1.appName.compareTo(o2.appName)
        }
        return result

    }

    private fun convertResolveInfo(resolveInfo: ResolveInfo): ItemApp {
        return ItemApp(
            getAppName(resolveInfo.activityInfo.packageName),
            resolveInfo.activityInfo.packageName,
            false,
            -1,
            -1,
            getAppIconByAppName(resolveInfo.activityInfo.packageName)
        )
    }

    fun getRunningApp(): UsageStats? {
        val usageStatsManager =
            application.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val mCurrentTime = System.currentTimeMillis()
        val mStartTime = mCurrentTime - 1000 * 60
        val mUsageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_BEST,
            mStartTime,
            mCurrentTime
        )
        var mRecentStat: UsageStats? = null
        for (usageStats in mUsageStats) {
            if (mRecentStat == null || mRecentStat.lastTimeUsed < usageStats.lastTimeUsed) {
                mRecentStat = usageStats
            }
        }
        return mRecentStat
    }

    fun getShutDownApps(): List<String> {
        val result = mutableListOf<String>()
        val usage =
            application.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 5000
        val event = UsageEvents.Event()
        val usageEvents = usage.queryEvents(beginTime, endTime)

        while (usageEvents.hasNextEvent()) {
            usageEvents.getNextEvent(event)
            if (event.eventType == 24) {
                result.add(event.packageName)
            }
        }
        return result
    }

    fun getLauncherPackageName(): String {
        val localPackageManager = packageManager
        val intent = Intent("android.intent.action.MAIN")
        intent.addCategory("android.intent.category.HOME")
        return localPackageManager
            .resolveActivity(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )!!.activityInfo.packageName
    }

    fun convertDrawableToBitmap(resource: Drawable): Bitmap {
        return resource.toBitmap()
    }

    fun resizeBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
        val ratioX = newWidth / bitmap.width.toFloat()
        val ratioY = newHeight / bitmap.height.toFloat()
        val middleX = newWidth / 2.0f
        val middleY = newHeight / 2.0f
        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)
        val canvas = Canvas(scaledBitmap)
        canvas.setMatrix(scaleMatrix)
        canvas.drawBitmap(
            bitmap,
            middleX - bitmap.width / 2,
            middleY - bitmap.height / 2,
            Paint(Paint.FILTER_BITMAP_FLAG)
        )
        return scaledBitmap
    }

}

fun getPercentageUsage(itemApp: ItemApp, queryType: AppQueryEnum): Float {
    val total = when(queryType){
        AppQueryEnum.ONE_DAY -> {
            getTodayTime()
        }
        AppQueryEnum.SEVEN_DAY -> {
            getOneDayTime() * 7
        }
        AppQueryEnum.THIRTY_DAY -> {
            getOneDayTime() * 30
        }
    }
    val usage = itemApp.timeUsedInDay
    val percentage = usage.toFloat() / total.toFloat() * 100
    return percentage
}