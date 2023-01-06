package com.b18dccn562.phonemanager.utils

import java.util.*

fun getTodayTimeStamp(): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    return calendar.time.time
}

fun getTodayTime(): Long {
    return System.currentTimeMillis() - getTodayTimeStamp()
}

fun getOneDayTime(): Long {
    return 1000 * 60 * 60 * 24
}

