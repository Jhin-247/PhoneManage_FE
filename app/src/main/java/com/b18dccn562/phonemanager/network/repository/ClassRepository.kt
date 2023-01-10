package com.b18dccn562.phonemanager.network.repository

import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.network.dto.ClassRequestDTO
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.network.services.ClassService
import kotlinx.coroutines.Deferred
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

    suspend fun requestJoinClass(itemClass: ClassDTO, userEmail: String): BaseResponse<Any> {
        return classService.requestJoinClassAsync(itemClass.id, userEmail).await()
    }

    suspend fun acceptJoinClass(requestId: Long): BaseResponse<List<ClassRequestDTO>> {
        return classService.acceptJoinClassAsync(requestId).await()
    }

    suspend fun denyJoinClass(requestId: Long): BaseResponse<List<ClassRequestDTO>> {
        return classService.denyJoinClassAsync(requestId).await()
    }

    suspend fun removeFromClass(
        classId: Long,
        studentId: Long
    ): BaseResponse<List<UserDTO>> {
        return classService.removeFromClassAsync(classId, studentId).await()
    }

    suspend fun banFromClass(
        classId: Long,
        studentId: Long
    ): BaseResponse<List<UserDTO>> {
        return classService.banFromClassAsync(classId, studentId).await()
    }

    suspend fun loadRequestForClass(
        classId: Long
    ): BaseResponse<List<ClassRequestDTO>> {
        return classService.loadRequestForClassAsync(classId).await()
    }

}