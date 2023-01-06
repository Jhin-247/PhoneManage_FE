package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.app_limit.AppLimitListFragment
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.app_list.AppListFragment
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.app_lock.AppLockListFragment

class AllAppTabAdapter(
    private val fragment: Fragment
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        if(position == 0){
            return AppListFragment()
        }
        if(position == 1){
            return AppLockListFragment()
        }
        return AppLimitListFragment()
    }

    fun getTitle(position: Int): String {
        if (position == 0) {
            return fragment.getString(R.string.app_list)
        }
        if(position == 1){
            return fragment.getString(R.string.lock_list)
        }
        return fragment.getString(R.string.limit_list)
    }
}