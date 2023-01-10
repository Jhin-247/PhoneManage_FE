package com.b18dccn562.phonemanager.presentation.screen.main.fragments.join_class

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentSearchClassBinding
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management.ClassManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchClassFragment : BaseFragment<FragmentSearchClassBinding>(),
    SearchClassAdapter.SearchClassClick {
    private val mClassManagementViewModel by activityViewModels<ClassManagementViewModel>()

    @Inject
    lateinit var searchClassAdapter: SearchClassAdapter

    override fun getLayoutId(): Int = R.layout.fragment_search_class

    override fun initData() {
        searchClassAdapter.classClickListener = this
    }

    override fun initYourView() {
        mMainViewModel.hideHeaderAndFooter()
        mBinding.searchViewModel = mClassManagementViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner

        mBinding.rcvClasses.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.rcvClasses.adapter = searchClassAdapter
    }

    override fun initListener() {
        mBinding.btnBack.setOnClickListener {
            navigateBack()
        }
        mBinding.btnSearch.setOnClickListener {
            mClassManagementViewModel.search(mMainViewModel.getUserEmail()!!)
        }
    }

    override fun initObserver() {
        mClassManagementViewModel.searchStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    searchClassAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
//                    searchClassAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        searchClassAdapter.submitList(it.data.data!!)
                    } else {
                        searchClassAdapter.submitList(listOf())
                    }
                }
            }
        }
        mClassManagementViewModel.joinClassProcess.observe(viewLifecycleOwner) {
            if (it == 0) {
                showLoadingDialog()
            }
            if (it == 1) {
                hideLoadingDialog()
                navigateBack()
                mClassManagementViewModel.resetJoinClassProcess()
            }
            if (it == 2) {
                hideLoadingDialog()
                showErrorSnackbar("Something went wrong")
            }
        }

        //ccab7dc1-89b6-443b-a3ae-62c509372c51
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainViewModel.showHeaderAndFooter()
    }

    override fun onClassClick(itemClass: ClassDTO) {
        mClassManagementViewModel.requestJoinClass(itemClass, mMainViewModel.getUserEmail()!!)
    }
}