package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management.teacher

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentTeacherClassManagementBinding
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management.ClassManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TeacherClassManagement : BaseFragment<FragmentTeacherClassManagementBinding>(),
    TeacherClassListAdapter.ClassClick {

    private val mClassManagementViewModel by activityViewModels<ClassManagementViewModel>()

    @Inject
    lateinit var classAdapter: TeacherClassListAdapter

    override fun getLayoutId(): Int = R.layout.fragment_teacher_class_management

    override fun initData() {
        mClassManagementViewModel.loadMyTeacherClass(mMainViewModel.getUserEmail()!!)
        classAdapter.classClickListener = this
    }

    override fun initYourView() {
        mBinding.rcvClass.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.rcvClass.adapter = classAdapter
    }

    override fun initListener() {
        mBinding.btnAdd.setOnClickListener {
            val direction =
                TeacherClassManagementDirections.actionTeacherClassManagementToCreateClassFragment()
            navigateToDirection(direction)
        }
    }

    override fun initObserver() {
        mClassManagementViewModel.loadClassState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    classAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    classAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        classAdapter.submitList(it.data.data!!)
                    } else {
                        classAdapter.submitList(listOf())
                    }
                }
            }
        }
    }

    override fun onClassClick(itemClass: ClassDTO) {
        val direction =
            TeacherClassManagementDirections.actionTeacherClassManagementToTeacherClassDetailFragment2(
                itemClass
            )
        navigateToDirection(direction)
    }
}