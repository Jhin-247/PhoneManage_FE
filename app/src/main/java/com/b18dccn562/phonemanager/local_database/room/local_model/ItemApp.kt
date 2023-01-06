package com.b18dccn562.phonemanager.local_database.room.local_model

import android.graphics.drawable.Drawable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "app")
data class ItemApp(
    var appName: String = "",
    @PrimaryKey
    var packageName: String = "",
    var isLock: Boolean = false,
    var timeLimited: Long = -1,
    var timeUsedInDay: Long = -1,
    @Ignore
    var appIcon: Drawable? = null,
    @Ignore
    var currentTotalTime: Long = 1000000
)