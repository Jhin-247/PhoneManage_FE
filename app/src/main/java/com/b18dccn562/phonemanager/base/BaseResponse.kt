package com.b18dccn562.phonemanager.base

class BaseResponse<T> {
    var code: Int = 0
    var message: String = ""
    var data: T? = null
}