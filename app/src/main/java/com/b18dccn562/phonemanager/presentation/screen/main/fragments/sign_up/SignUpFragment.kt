package com.b18dccn562.phonemanager.presentation.screen.main.fragments.sign_up

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.base.BaseResponse
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentSignUpBinding
import com.b18dccn562.phonemanager.network.dto.UserDTO
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : BaseFragment<FragmentSignUpBinding>() {

    private val args: SignUpFragmentArgs by navArgs()
    private val mSignUpViewModel by viewModels<SignUpViewModel>()

    private var role = -1

    override fun getLayoutId(): Int = R.layout.fragment_sign_up

    override fun initData() {
        role = args.role
        mSignUpViewModel.setRole(role)
    }

    override fun initYourView() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.signupViewModel = mSignUpViewModel
    }

    override fun initListener() {
        with(mBinding) {
            tvPickAnotherRole.setOnClickListener {
                navigateBack()
            }
        }
    }

    override fun initObserver() {
        mSignUpViewModel.isReadyToSignup.observe(viewLifecycleOwner) {
            if (it == true) {
                mMainViewModel.doSignup(
                    mSignUpViewModel.getEmail(),
                    mSignUpViewModel.getPassword(),
                    mSignUpViewModel.getFullName(),
                    role
                )
            }
        }
        mMainViewModel.signupState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Error -> {
                    hideLoadingDialog()
//                    showErrorSnackbar(it.exception!!.message!!)
                    mMainViewModel.resetSignupState()
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    handleSignupResponse(it.data!!)
                }
                else -> {
                    clearData()
                }
            }
        }
    }

    private fun clearData() = with(mBinding) {
        etFullName.setText("")
        etEmail.setText("")
        etPassword.setText("")
        etRePassword.setText("")
    }

    private fun handleSignupResponse(data: BaseResponse<UserDTO>) {
        when (data.code) {
            Constants.ResponseCode.SUCCESS -> {
                val direction =
                    SignUpFragmentDirections.actionSignUpFragmentToChangeUserInformationFragment()
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