package com.b18dccn562.phonemanager.network.dto

import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp

data class AppUsageDTO(
    var id: Long = 0L,
    var user: UserDTO? = null,
    var app: ItemApp? = null,
    var time: Long = 0L,
    var action: Int = 0
)