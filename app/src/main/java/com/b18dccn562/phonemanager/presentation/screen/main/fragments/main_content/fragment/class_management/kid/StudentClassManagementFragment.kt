package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management.kid

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentStudentClassManagementBinding
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management.ClassManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentClassManagementFragment : BaseFragment<FragmentStudentClassManagementBinding>(),
    StudentClassListAdapter.ClassClick {

    private val mClassManagementViewModel by activityViewModels<ClassManagementViewModel>()

    @Inject
    lateinit var studentClassAdapter: StudentClassListAdapter

    override fun getLayoutId(): Int = R.layout.fragment_student_class_management

    override fun initData() {
        mClassManagementViewModel.loadMyStudentClass(mMainViewModel.getUserEmail()!!)
        studentClassAdapter.classClickListener = this
    }

    override fun initYourView() {
        mBinding.rcvClass.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.rcvClass.adapter = studentClassAdapter
    }

    override fun initListener() {
        mBinding.btnAdd.setOnClickListener {
            val direction =
                StudentClassManagementFragmentDirections.actionStudentClassManagementFragmentToSearchClassFragment()
            navigateToDirection(direction)
        }
    }

    override fun initObserver() {
        mClassManagementViewModel.loadKidClassState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    studentClassAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    studentClassAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        studentClassAdapter.submitList(it.data.data!!)
                    } else {
                        studentClassAdapter.submitList(listOf())
                    }
                }
            }
        }
    }

    override fun onClassClick(itemClass: ClassDTO) {
        val direction =
            StudentClassManagementFragmentDirections.actionStudentClassManagementFragmentToStudentClassDetailFragment(
                itemClass
            )
        navigateToDirection(direction)
    }
}