package com.b18dccn562.phonemanager.network.services

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.network.dto.UserDTO
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface ClassService {

    @POST(Constants.ApiReferences.CLASS_REFERENCE + Constants.ApiReferences.CREATE_CLASS)
    fun createNewClassAsync(
        @Query("classname") classname: String,
        @Query("description") description: String,
        @Query("subject") subject: String,
        @Query("grade") grade: Int,
        @Query("teacher") teacherEmail: String
    ): Deferred<BaseResponse<ClassDTO>>


    @GET(Constants.ApiReferences.CLASS_REFERENCE + Constants.ApiReferences.GET_TEACHER_CLASSES)
    fun getAllTeacherClassAsync(
        @Query("teacher") teacherEmail: String
    ): Deferred<BaseResponse<List<ClassDTO>>>

    @GET(Constants.ApiReferences.CLASS_REFERENCE + Constants.ApiReferences.GET_STUDENT_CLASSES)
    fun getAllKidClassAsync(
        @Query("student_email") studentEmail: String
    ): Deferred<BaseResponse<List<ClassDTO>>>

    @GET(Constants.ApiReferences.CLASS_REFERENCE + Constants.ApiReferences.GET_STUDENT_IN_CLASS)
    fun getStudentInClassAsync(
        @Query("class_id") classId: Long
    ): Deferred<BaseResponse<List<UserDTO>>>

    @GET(Constants.ApiReferences.CLASS_REFERENCE + Constants.ApiReferences.SEARCH_CLASS)
    fun searchClassAsync(
        @Query("search") search: String,
        @Query("user_email") userEmail: String
    ): Deferred<BaseResponse<List<ClassDTO>>>

    @POST(Constants.ApiReferences.CLASS_REFERENCE + Constants.ApiReferences.JOIN_CLASS)
    fun joinClassAsync(
        @Query("class_id") classId: Long,
        @Query("student_email") userEmail: String
    ): Deferred<BaseResponse<Any>>

}