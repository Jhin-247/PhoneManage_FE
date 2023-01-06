package com.b18dccn562.phonemanager.network.dto

data class ReportDTO(
    var id: Long = 0L,
    var appUsage: AppUsageDTO? = null,
    var studentClass: ClassDTO? = null,
    var time: Long = -1,
    var description: String = ""
)