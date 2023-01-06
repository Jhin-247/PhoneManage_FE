package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.setting

import android.view.View
import androidx.navigation.fragment.findNavController
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.databinding.FragmentSettingBinding
import com.b18dccn562.phonemanager.local_database.shared_preference.AccountPreference
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.sign_in.SignInFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_setting

    override fun initData() {
    }

    override fun initYourView() {
        val userRole = mMainViewModel.getUserRole()
        if (userRole != Constants.Role.PARENT) {
            mBinding.btnSubAccount.visibility = View.GONE
        }
    }

    override fun initListener() {
        with(mBinding) {
            btnAccount.setOnClickListener {
                val direction =
                    SettingFragmentDirections.actionSettingFragmentToAccountSettingFragment()
                navigateToDirection(direction)
            }
            btnLock.setOnClickListener {
                val direction =
                    SettingFragmentDirections.actionSettingFragmentToChangeLockFragment()
                navigateToDirection(direction)
            }
            btnLogOut.setOnClickListener {
                mMainViewModel.logout()
            }
            btnSubAccount.setOnClickListener {
                val direction =
                    SettingFragmentDirections.actionSettingFragmentToCreateSubAccountFragment()
                navigateToDirection(direction)
            }
        }
    }

    override fun initObserver() {
    }
}