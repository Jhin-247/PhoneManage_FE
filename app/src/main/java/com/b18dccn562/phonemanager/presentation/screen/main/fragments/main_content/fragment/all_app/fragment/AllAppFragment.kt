package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment

import androidx.fragment.app.activityViewModels
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.databinding.FragmentAllAppBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllAppFragment: BaseFragment<FragmentAllAppBinding>() {

    private lateinit var mAppTabAdapter: AllAppTabAdapter

    private val mAppViewModel by activityViewModels<AppViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_all_app

    override fun initData() {
        mAppTabAdapter = AllAppTabAdapter(this)
    }

    override fun initYourView() {
        mBinding.viewPager.adapter = mAppTabAdapter
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager) { tab, position ->
            tab.text = mAppTabAdapter.getTitle(position)
        }.attach()
    }

    override fun initListener() {
    }

    override fun initObserver() {
        mAppViewModel.appSetting.observe(viewLifecycleOwner){
            mAppViewModel.appSettingReload()
        }
    }
}