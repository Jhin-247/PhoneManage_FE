package com.b18dccn562.phonemanager.network.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ClassRequestDTO(
    var id: Long = 0L,
    var requester: UserDTO = UserDTO(),
    var classToJoin: ClassDTO = ClassDTO(),
    var time: Long = 0L
) : Parcelable