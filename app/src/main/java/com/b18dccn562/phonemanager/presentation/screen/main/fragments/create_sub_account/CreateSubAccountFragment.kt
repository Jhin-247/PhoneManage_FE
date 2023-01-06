package com.b18dccn562.phonemanager.presentation.screen.main.fragments.create_sub_account

import androidx.fragment.app.viewModels
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentCreateSubAccountBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateSubAccountFragment : BaseFragment<FragmentCreateSubAccountBinding>() {

    private val mCreateSubAccountViewModel by viewModels<SubAccountViewModel>()

    private var userRole = -1

    override fun getLayoutId(): Int = R.layout.fragment_create_sub_account

    override fun initData() {
    }

    override fun initYourView() {
        mBinding.signupViewModel = mCreateSubAccountViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner

        mMainViewModel.hideHeaderAndFooter()
    }

    override fun initListener() {
    }

    override fun initObserver() {
        mCreateSubAccountViewModel.createUserState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    navigateBack()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainViewModel.showHeaderAndFooter()
    }
}