package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.app_limit

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentAppLimitListBinding
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppLimitListFragment : BaseFragment<FragmentAppLimitListBinding>(),
    AppLimitAdapter.OnAppUnLimitClickListener {
    private val mAppViewModel by activityViewModels<AppViewModel>()

    @Inject
    lateinit var appLimitAdapter: AppLimitAdapter

    override fun getLayoutId(): Int = R.layout.fragment_app_limit_list

    override fun initData() {
        appLimitAdapter.unlockClickListener = this
    }

    override fun initYourView() = with(mBinding) {
        rcvApps.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        rcvApps.adapter = appLimitAdapter
    }

    override fun initListener() {
    }

    override fun initObserver() {
        mAppViewModel.appLimitList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    appLimitAdapter.submitList(listOf())
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    appLimitAdapter.submitList(it.data!!)
                }
            }
        }
    }

    override fun unLimit(item: ItemApp) {
        mAppViewModel.changeAppLimit(item, 0)
    }
}