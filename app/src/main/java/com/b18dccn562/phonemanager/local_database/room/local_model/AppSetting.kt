package com.b18dccn562.phonemanager.local_database.room.local_model

import androidx.room.Embedded
import androidx.room.Relation

data class AppSetting(
    @Embedded
    var app: ItemApp,
    @Relation(
        parentColumn = "packageName",
        entityColumn = "appPackageName"
    )
    var setting: Setting
)