package com.b18dccn562.phonemanager.presentation.screen.main.fragments.sign_in

import androidx.fragment.app.viewModels
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentSignInBinding
import com.b18dccn562.phonemanager.network.dto.UserDTO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>() {
    private val mSignInViewModel by viewModels<SignInViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_sign_in

    override fun initData() {
    }

    override fun initYourView() {
        mBinding.loginViewModel = mSignInViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initListener() {
        with(mBinding) {
            tvSignUp.setOnClickListener {
                val direction = SignInFragmentDirections.actionSignInFragmentToPickRoleFragment()
                navigateToDirection(direction)
            }
        }
    }

    override fun initObserver() {
        mSignInViewModel.isReadyToLogin.observe(viewLifecycleOwner) {
            if (it == true) {
                mMainViewModel.doLogin(mSignInViewModel.getEmail(), mSignInViewModel.getPassword())
            } else {
                showErrorSnackbar(getString(R.string.login_information_missing))
            }
        }
        mMainViewModel.loginState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    hideLoadingDialog()
//                    showErrorSnackbar("Error ${it.exception?.message}}")
                    mMainViewModel.resetLoginState()
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    handleLoginResponse(it.data!!)
                }
                else -> {
                    clearData()
                }
            }
        }
    }

    private fun clearData() = with(mBinding) {
        etEmail.setText("")
        etPassword.setText("")
    }

    private fun handleLoginResponse(data: BaseResponse<UserDTO>) {
        when (data.code) {
            Constants.ResponseCode.SUCCESS -> {
                val direction =
                    SignInFragmentDirections.actionSignInFragmentToChangeUserInformationFragment()
                navigateToDirection(direction)
            }
            Constants.ResponseCode.ERROR -> {
                showErrorSnackbar(data.message)
            }
            else -> {
                showErrorSnackbar(getString(R.string.unknown_error))
            }
        }
    }
}