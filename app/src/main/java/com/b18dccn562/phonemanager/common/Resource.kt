package com.b18dccn562.phonemanager.common

sealed class Resource<T>(
    val data: T? = null,
    val exception: Exception? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(
        message: String? = null,
//        data: T? = null,
        exception: Exception? = null,
//        errorCode: Int? = null
    ) :
        Resource<T>(exception = exception, message = message)

    class Empty<T>(): Resource<T>()

    class Loading<T>(data: T? = null) : Resource<T>(data)
}