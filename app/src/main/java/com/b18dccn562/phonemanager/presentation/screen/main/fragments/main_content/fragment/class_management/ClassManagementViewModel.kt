package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management

import android.util.Log
import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.network.repository.ClassRepository
import com.b18dccn562.phonemanager.service.Observer
import com.b18dccn562.phonemanager.service.ObserverForService
import com.b18dccn562.phonemanager.utils.endClass
import com.b18dccn562.phonemanager.utils.getRealtimeDatabase
import com.b18dccn562.phonemanager.utils.startClass
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClassManagementViewModel @Inject constructor(
    private val classRepository: ClassRepository
) : ViewModel() {

    private val job = Job()

    private val scope = CoroutineScope(Dispatchers.IO + job)

    val classname = MutableLiveData<String>()
    val subject = MutableLiveData<String>()
    val grade = MutableLiveData<Int>()
    val teacher = MutableLiveData<String>()

    val searchText = MutableLiveData<String>()

    private val _isClassStarting = MutableLiveData<Boolean>()

    private var currentClassId: Long? = null

    private var listener: ValueEventListener? = null

    private val _createClassState = MutableLiveData<Resource<BaseResponse<ClassDTO>>>()
    val createClassState: LiveData<Resource<BaseResponse<ClassDTO>>> = _createClassState

    private val _loadClassState = MutableLiveData<Resource<BaseResponse<List<ClassDTO>>>>()
    val loadClassState: LiveData<Resource<BaseResponse<List<ClassDTO>>>> = _loadClassState

    private val _studentInClass = MutableLiveData<Resource<BaseResponse<List<UserDTO>>>>()
    val studentInClass: LiveData<Resource<BaseResponse<List<UserDTO>>>> = _studentInClass

    private val _loadKidClassState = MutableLiveData<Resource<BaseResponse<List<ClassDTO>>>>()
    val loadKidClassState: LiveData<Resource<BaseResponse<List<ClassDTO>>>> = _loadKidClassState

    private val _searchStatus = MutableLiveData<Resource<BaseResponse<List<ClassDTO>>>>()
    val searchStatus: LiveData<Resource<BaseResponse<List<ClassDTO>>>> = _searchStatus

    private val _joinClassProcess = MutableLiveData(-1)
    val joinClassProcess: LiveData<Int> = _joinClassProcess

    fun handleGradeInput(editText: EditText) {
        val classGrade = editText.text.toString().toInt()
        grade.value = classGrade
    }

    fun create() {
        if (allConditionAreFullFill()) {
            createClass()
        }
    }

    private fun createClass() {
        val inputClassname = classname.value!!
        val inputSubject = subject.value!!
        val inputGrade = grade.value!!
        val inputTeacher = teacher.value!!

        scope.launch {
            _createClassState.postValue(Resource.Loading())
            try {
                val result = classRepository.createClass(
                    inputClassname,
                    "",
                    inputSubject,
                    inputGrade,
                    inputTeacher
                )
                _createClassState.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _createClassState.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

    private fun allConditionAreFullFill(): Boolean {
        val inputClassname = classname.value
        val inputSubject = subject.value
        val inputGrade = grade.value
        val inputTeacher = teacher.value
        if (inputClassname == null || inputClassname.isEmpty()) {
            return false
        }
        if (inputSubject == null || inputSubject.isEmpty()) {
            return false
        }
        if (inputGrade == null) {
            return false
        }
        if (inputTeacher == null || inputTeacher.isEmpty()) {
            return false
        }
        return true
    }

    fun loadMyTeacherClass(userEmail: String) {
        scope.launch {
            _loadClassState.postValue(Resource.Loading())
            try {
                val result = classRepository.getAllTeacherClass(userEmail)
                _loadClassState.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _loadClassState.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

    fun loadMyStudentClass(userEmail: String) {
        scope.launch {
            _loadKidClassState.postValue(Resource.Loading())
            try {
                val result = classRepository.getAllKidClass(userEmail)
                _loadKidClassState.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _loadKidClassState.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

    fun getClassDetail(classDTO: ClassDTO) {
        getStudentsInClass(classDTO)
    }

    private fun getStudentsInClass(classDTO: ClassDTO) {
        scope.launch {
            _studentInClass.postValue(Resource.Loading())
            try {
                val result = classRepository.getStudentInClass(classDTO.id)
                _studentInClass.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _studentInClass.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }

        }
    }

    fun search(userEmail: String) {
        scope.launch {
            _searchStatus.postValue(Resource.Loading())
            try {
                val result = classRepository.searchClass(searchText.value ?: "", userEmail)
                _searchStatus.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _searchStatus.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

    fun joinClass(itemClass: ClassDTO, userEmail: String) {
        scope.launch {
            _joinClassProcess.postValue(0)
            try {
                val result = classRepository.joinClass(itemClass, userEmail)
                if (result.code == 200)
                    _joinClassProcess.postValue(1)
                else
                    _joinClassProcess.postValue(2)
            } catch (exception: Exception) {
                _joinClassProcess.postValue(2)
            }
        }
    }

    fun resetJoinClassProcess() {
        _joinClassProcess.value = -1
    }

    fun changeClass(classId: Long) {
        if (currentClassId != null) {
            removeOldListener(currentClassId!!)
        }
        currentClassId = classId
        listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.value == true) {
                    _isClassStarting.postValue(true)
                    Log.d("OkHttp----", "Debugging ${_isClassStarting.value}")
                } else {
                    _isClassStarting.postValue(false)
                    Constants.classId = -1
                }
                Log.d("OkHttp----", "${snapshot.value}")
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }
        val ref = "Class"
        getRealtimeDatabase().getReference(ref).child(currentClassId.toString()).child("is_started")
            .addValueEventListener(listener!!)
    }

    fun removeOldListener(classId: Long) {
        val ref = "Class"
        listener?.let {
            getRealtimeDatabase().getReference(ref).child(classId.toString()).child("is_started")
                .removeEventListener(it)
            listener = null
        }
    }


    fun startOrEndClass(classDTO: ClassDTO) {
        val canStartClass = _isClassStarting.value
        Log.d("OkHttp----", "$canStartClass")
        if (canStartClass == null || canStartClass == false) {
            startClass(classDTO.id)
        } else {
            endClass(classDTO.id)
        }
    }

    fun joinSession(classDTO: ClassDTO) {
        val canJoinClass = _isClassStarting.value
        if (canJoinClass == true) {
            Constants.classId = classDTO.id
            ObserverForService.update()
        }
    }

}