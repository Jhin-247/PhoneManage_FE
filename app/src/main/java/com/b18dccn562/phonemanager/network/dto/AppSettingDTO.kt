package com.b18dccn562.phonemanager.network.dto

import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp

data class AppSettingDTO(
    var id: Long = 0L,
    var user: UserDTO? = null,
    var app: ItemApp? = null,
    var isLock: Boolean = false,
    var isLimited: Long = 0L
)