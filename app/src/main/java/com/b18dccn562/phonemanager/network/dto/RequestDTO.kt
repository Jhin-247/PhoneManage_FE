package com.b18dccn562.phonemanager.network.dto

open class RequestDTO {
    var id = 0L
    var requester = UserDTO()
    var time: Long = 0L
    var action: Int = 0
    var child = UserDTO()
}