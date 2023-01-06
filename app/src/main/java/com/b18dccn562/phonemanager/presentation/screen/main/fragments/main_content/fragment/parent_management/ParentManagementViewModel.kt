package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.parent_management

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.local_database.shared_preference.AccountPreference
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.network.repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParentManagementViewModel @Inject constructor(
    private val accountUtils: AccountPreference,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _childrenInfo = MutableLiveData<List<UserDTO>?>()
    val childrenInfo: LiveData<List<UserDTO>?> = _childrenInfo

    private val job = Job()

    private val scope = CoroutineScope(Dispatchers.IO + job)

    private val _partnerState = MutableLiveData<Resource<BaseResponse<UserDTO>>>()
    val partnerState: LiveData<Resource<BaseResponse<UserDTO>>> = _partnerState

    private val _childrenState = MutableLiveData<Resource<BaseResponse<List<UserDTO>>>>()
    val childrenState: LiveData<Resource<BaseResponse<List<UserDTO>>>> = _childrenState

    private var email: String = ""

    init {
        email = accountUtils.getEmail()
        getPartnerInfo()

        getChildrenInfo()
    }

    private fun getChildrenInfo() {
//        parentService.getChildrenInfo(email).enqueue(object : Callback<BaseResponse<List<User>>> {
//            override fun onResponse(
//                call: Call<BaseResponse<List<User>>>,
//                response: Response<BaseResponse<List<User>>>
//            ) {
//                if (response.body()!!.code == Constants.ResponseCode.SUCCESS) {
//                    _childrenInfo.value = response.body()!!.data
//                } else {
//                    _childrenInfo.value = null
//                }
//            }
//
//            override fun onFailure(call: Call<BaseResponse<List<User>>>, t: Throwable) {
//
//            }
//
//        })
    }

    private fun getPartnerInfo() {
//        parentService.getPartnerInfo(email).enqueue(object : Callback<BaseResponse<User>> {
//            override fun onResponse(
//                call: Call<BaseResponse<User>>,
//                response: Response<BaseResponse<User>>
//            ) {
//                if (response.body()!!.code == Constants.ResponseCode.SUCCESS) {
//                    _partnerInfo.value = response.body()!!.data
//                } else {
//                    _partnerInfo.value = null
//                }
//            }
//
//            override fun onFailure(call: Call<BaseResponse<User>>, t: Throwable) {
//            }
//
//        })
    }


    fun loadPartner() {
        scope.launch {
            _partnerState.postValue(Resource.Loading())
            try {
                val result = accountRepository.getPartnerInfo(email)
                _partnerState.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _partnerState.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

    fun loadChildren() {
        scope.launch {
            _childrenState.postValue(Resource.Loading())
            try {
                val result = accountRepository.getChildrenInfo(email)
                _childrenState.postValue(Resource.Success(result))
            } catch (exception: Exception) {
                _childrenState.postValue(Resource.Error("Something went wrong ${exception.message}"))
            }
        }
    }

}