package com.b18dccn562.phonemanager.local_database.room.local_model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_daily_usage")
data class DailyUsage(
    @PrimaryKey
    var packageName: String = "",
    var timeUsedInDay: Long = 0L
)