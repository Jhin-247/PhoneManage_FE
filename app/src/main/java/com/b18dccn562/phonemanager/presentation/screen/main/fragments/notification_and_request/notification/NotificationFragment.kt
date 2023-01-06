package com.b18dccn562.phonemanager.presentation.screen.main.fragments.notification_and_request.notification

import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentNotificationBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    private val notificationViewModel by activityViewModels<NotificationViewModel>()

    @Inject
    lateinit var notificationAdapter: NotificationAdapter

    override fun getLayoutId(): Int = R.layout.fragment_notification

    override fun initData() {
        notificationViewModel.getNotifications(mMainViewModel.getUserEmail()!!)
    }

    override fun initYourView() {
        mBinding.rcvNotification.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.rcvNotification.adapter = notificationAdapter
    }

    override fun initListener() {
        mBinding.btnBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun initObserver() {
        notificationViewModel.loadNotificationState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    notificationAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    notificationAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        notificationAdapter.submitList(it.data.data!!)
                        Log.d("data", it.data.data!![0].content)
                    } else {
                        notificationAdapter.submitList(listOf())
                    }
                }
            }
        }
    }
}