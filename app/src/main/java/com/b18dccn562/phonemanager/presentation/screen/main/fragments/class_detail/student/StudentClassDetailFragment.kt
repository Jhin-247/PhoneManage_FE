package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_detail.student

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentStudentClassDetailBinding
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_detail.teacher.StudentListAdapter
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management.ClassManagementViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StudentClassDetailFragment : BaseFragment<FragmentStudentClassDetailBinding>() {

    private val mClassManagementViewModel by activityViewModels<ClassManagementViewModel>()

    @Inject
    lateinit var studentListAdapter: StudentListAdapter

    private lateinit var classDTO: ClassDTO

    override fun getLayoutId(): Int = R.layout.fragment_student_class_detail

    override fun initData() {
        retrieveData()
    }

    private fun retrieveData() {
        val bundle = arguments
        if (bundle == null) {
            Log.e("Confirmation", "ConfirmationFragment did not receive traveler information")
            return
        }
        val args = StudentClassDetailFragmentArgs.fromBundle(bundle)
        classDTO = args.classInfo
        mBinding.classDetail = classDTO
        mClassManagementViewModel.getClassDetail(classDTO)
        mClassManagementViewModel.changeClass(classDTO.id)
    }

    override fun initYourView() {
        mMainViewModel.hideHeaderAndFooter()
        mBinding.lifecycleOwner = viewLifecycleOwner

        mBinding.rcvStudent.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.rcvStudent.adapter = studentListAdapter
    }

    override fun initListener() {
        mBinding.btnBack.setOnClickListener {
            navigateBack()
        }
        mBinding.btnStartEndClass.setOnClickListener {
            mClassManagementViewModel.joinSession(classDTO)
        }
    }

    override fun initObserver() {
        mClassManagementViewModel.studentInClass.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    studentListAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    studentListAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        studentListAdapter.submitList(it.data.data!!)
                    } else {
                        studentListAdapter.submitList(listOf())
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainViewModel.showHeaderAndFooter()
        mClassManagementViewModel.removeOldListener(classDTO.id)
    }
}