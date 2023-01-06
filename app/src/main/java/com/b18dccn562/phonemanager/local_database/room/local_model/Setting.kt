package com.b18dccn562.phonemanager.local_database.room.local_model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_setting")
data class Setting(
    @PrimaryKey(autoGenerate = true)
    var settingId: Long = 0,
    var isLock: Boolean,
    var isLimited: Long,
    var appPackageName: String
)

