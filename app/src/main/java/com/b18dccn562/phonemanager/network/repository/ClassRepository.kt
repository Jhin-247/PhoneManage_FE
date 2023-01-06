package com.b18dccn562.phonemanager.network.repository

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.network.services.ClassService
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassRepository @Inject constructor(
    private val classService: ClassService
) {

    suspend fun createClass(
        classname: String,
        description: String,
        subject: String,
        grade: Int,
        teacherEmail: String
    ): BaseResponse<ClassDTO> {
        return classService.createNewClassAsync(
            classname,
            description,
            subject,
            grade,
            teacherEmail
        ).await()
    }

    suspend fun getAllTeacherClass(
        email: String
    ): BaseResponse<List<ClassDTO>> {
        return classService.getAllTeacherClassAsync(email).await()
    }

    suspend fun getAllKidClass(
        email: String
    ): BaseResponse<List<ClassDTO>> {
        return classService.getAllKidClassAsync(email).await()
    }

    suspend fun getStudentInClass(
        classId: Long
    ): BaseResponse<List<UserDTO>> {
        return classService.getStudentInClassAsync(classId).await()
    }

    suspend fun searchClass(search: String, userEmail: String): BaseResponse<List<ClassDTO>> {
        return classService.searchClassAsync(search, userEmail).await()
    }

    suspend fun joinClass(itemClass: ClassDTO, userEmail: String): BaseResponse<Any> {
        return classService.joinClassAsync(itemClass.id, userEmail).await()
    }

}