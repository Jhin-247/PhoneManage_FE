package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_request

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentClassRequestBinding
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClassRequestFragment : BaseFragment<FragmentClassRequestBinding>(),
    ClassRequestAdapter.RequestClickListener {

    private val classRequestViewModel by viewModels<ClassRequestViewModel>()

    @Inject
    lateinit var classRequestAdapter: ClassRequestAdapter

    private lateinit var classDTO: ClassDTO

    override fun getLayoutId(): Int = R.layout.fragment_class_request

    override fun initData() {
        retrieveData()
        classRequestAdapter.requestClickListener = this
    }

    private fun retrieveData() {
        val bundle = arguments
        if (bundle == null) {
            Log.e("Confirmation", "ConfirmationFragment did not receive traveler information")
            return
        }
        val args = ClassRequestFragmentArgs.fromBundle(bundle)
        classDTO = args.classRequest
        mBinding.myClass = classDTO
        classRequestViewModel.loadClassRequest(classDTO.id)
    }

    override fun initYourView() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.rcvRequests.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.rcvRequests.adapter = classRequestAdapter
    }

    override fun initListener() {
        mBinding.btnBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun initObserver() {
        classRequestViewModel.loadClassRequestStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    classRequestAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    classRequestAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                    classRequestAdapter.submitList(listOf())
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        classRequestAdapter.submitList(it.data.data!!)
                    } else {
                        classRequestAdapter.submitList(listOf())
                    }
                }
            }
        }
    }

    override fun onAcceptClick(requestId: Long) {
        classRequestViewModel.acceptRequest(requestId)
    }

    override fun onDenyClick(requestId: Long) {
        classRequestViewModel.denyRequest(requestId)
    }
}