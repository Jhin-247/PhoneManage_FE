package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.app_lock

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentAppLockListBinding
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppLockListFragment : BaseFragment<FragmentAppLockListBinding>(),
    AppLockAdapter.OnAppUnLockClickListener {

    private val mAppViewModel by activityViewModels<AppViewModel>()

    @Inject
    lateinit var appLockAdapter: AppLockAdapter


    override fun getLayoutId(): Int = R.layout.fragment_app_lock_list

    override fun initData() {
        appLockAdapter.unlockClickListener = this
    }

    override fun initYourView() = with(mBinding) {
        rcvApps.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        rcvApps.adapter = appLockAdapter
    }

    override fun initListener() {
    }

    override fun initObserver() {
        mAppViewModel.appLockList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    appLockAdapter.submitList(listOf())
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    appLockAdapter.submitList(it.data!!)
                }
            }
        }
    }

    override fun unlock(item: ItemApp) {
        mAppViewModel.lockOrUnLockApp(item)
    }
}