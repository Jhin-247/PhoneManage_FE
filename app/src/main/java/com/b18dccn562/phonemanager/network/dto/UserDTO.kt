package com.b18dccn562.phonemanager.network.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserDTO(
    var id: Long = 0L,
    var username: String = "",
    var role: Int = 0,
    var email: String = "",
    var password: String = "",
    var accessToken: String = "",
    var avatarUrl: String = "",
    var uid: String = ""
) : Parcelable