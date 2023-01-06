package com.b18dccn562.phonemanager.network.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class ClassDTO(
    var id: Long = 0L,
    var timeCreated: Long = 0,
    var classname: String = "",
    var description: String = "",
    var classId: UUID = UUID.randomUUID(),
    var subject: String = "",
    var grade: Int = 0,
    var teacher: UserDTO = UserDTO()
) : Parcelable