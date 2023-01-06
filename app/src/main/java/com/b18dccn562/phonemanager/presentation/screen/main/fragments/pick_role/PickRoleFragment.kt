package com.b18dccn562.phonemanager.presentation.screen.main.fragments.pick_role

import android.view.View
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.databinding.FragmentPickRoleBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PickRoleFragment : BaseFragment<FragmentPickRoleBinding>() {
    override fun getLayoutId(): Int = R.layout.fragment_pick_role

    override fun initData() {
    }

    override fun initYourView() {
    }

    override fun initListener() {
        with(mBinding) {
            btnParent.setOnClickListener {
                navigateToSignUpFragment(it, Constants.Role.PARENT)
            }
            btnKid.setOnClickListener {
                navigateToSignUpFragment(it, Constants.Role.CHILD)
            }
            btnTeacher.setOnClickListener {
                navigateToSignUpFragment(it, Constants.Role.TEACHER)
            }
        }
    }

    override fun initObserver() {
    }

    private fun navigateToSignUpFragment(view: View, accountType: Int) {
        val action = PickRoleFragmentDirections.actionPickRoleFragmentToSignUpFragment(accountType)
        navigateToDirection(action)
    }
}