package com.b18dccn562.phonemanager.presentation.screen.main.fragments.create_class

import androidx.fragment.app.activityViewModels
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentCreateClassBinding
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management.ClassManagementViewModel
import com.b18dccn562.phonemanager.utils.createClass
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateClassFragment : BaseFragment<FragmentCreateClassBinding>() {

    private val mClassManagementViewModel by activityViewModels<ClassManagementViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_create_class

    override fun initData() {
        mBinding.myClass = mClassManagementViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner

        mClassManagementViewModel.teacher.value = mMainViewModel.getUserEmail()
    }

    override fun initYourView() {
        mMainViewModel.hideHeaderAndFooter()
    }

    override fun initListener() = with(mBinding) {
        btnBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun initObserver() {
        mClassManagementViewModel.createClassState.observe(viewLifecycleOwner) {
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
                    if (it.data!!.code == 200) {
                        navigateBack()
                        createClass(it.data.data!!.id)
                    } else {
                        showErrorSnackbar("Error: ${it.message}")
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainViewModel.showHeaderAndFooter()
    }
}