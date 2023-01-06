package com.b18dccn562.phonemanager.utils.enum_data

import com.b18dccn562.phonemanager.common.Constants
import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun getOneDayBeforeMilliSeconds(): Long {
        val currentMilli = System.currentTimeMillis()
        return currentMilli - Constants.ONE_DAY_MILLISECOND_COUNT
    }

    fun getThreeDayBeforeMilliSecond(): Long {
        return System.currentTimeMillis() - 3 * Constants.ONE_DAY_MILLISECOND_COUNT
    }

    fun getSevenDayBeforeMilliSecond(): Long {
        return System.currentTimeMillis() - 7 * Constants.ONE_DAY_MILLISECOND_COUNT
    }

    fun getThirdTyDayBeforeMilliSecond(): Long {
        return System.currentTimeMillis() - 30 * Constants.ONE_DAY_MILLISECOND_COUNT
    }

    fun convertMillSecondToString(time: Long): String {
        val second = (time / 1000).toInt()
        val mMinute = second / 60
        val mHour = mMinute / 60
        val mFinalMinute = mMinute - mHour * 60
        val mFinalSecond = second - mMinute * 60
        return "${mHour}h ${mFinalMinute}m ${mFinalSecond}s"
//        return time.toString()
    }

    fun convertLongToTime(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.US)
        return format.format(date)
    }

    fun currentTimeToLong(): Long {
        return System.currentTimeMillis()
    }

    fun convertDateToLong(date: String): Long {
        val df = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.US)
        return df.parse(date)!!.time
    }


}