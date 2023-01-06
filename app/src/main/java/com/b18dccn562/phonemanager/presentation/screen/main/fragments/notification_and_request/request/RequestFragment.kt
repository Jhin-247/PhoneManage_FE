package com.b18dccn562.phonemanager.presentation.screen.main.fragments.notification_and_request.request

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentRequestBinding
import com.b18dccn562.phonemanager.network.dto.RequestDTO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RequestFragment : BaseFragment<FragmentRequestBinding>(), RequestAdapter.RequestClick {

    private val requestViewModel by activityViewModels<RequestViewModel>()

    @Inject
    lateinit var requestAdapter: RequestAdapter

    override fun getLayoutId(): Int = R.layout.fragment_request

    override fun initData() {
        requestViewModel.loadRequests(mMainViewModel.getUserEmail()!!)
        requestAdapter.requestListener = this
    }

    override fun initYourView() {
        mBinding.rcvRequests.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.rcvRequests.adapter = requestAdapter
    }

    override fun initListener() {
        mBinding.btnBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun initObserver() {
        requestViewModel.loadRequestState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    requestAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    requestAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        requestAdapter.submitList(it.data.data!!)
                    } else {
                        requestAdapter.submitList(listOf())
                    }
                }
            }
        }
    }

    override fun onAcceptRequest(requestDTO: RequestDTO) {
        requestViewModel.acceptRequest(requestDTO)
    }

    override fun onDenyRequest(requestDTO: RequestDTO) {
        requestViewModel.denyRequest(requestDTO)
    }
}