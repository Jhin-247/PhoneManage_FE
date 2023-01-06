package com.b18dccn562.phonemanager.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavDirections
import com.b18dccn562.phonemanager.presentation.screen.main.MainViewModel
import com.b18dccn562.phonemanager.utils.navigateTo
import com.b18dccn562.phonemanager.utils.navigateUp
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment<VB : ViewDataBinding> : Fragment() {
    lateinit var mBinding: VB

    protected val mMainViewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater,
            getLayoutId(),
            container,
            false
        )
        initData()
        initYourView()
        initListener()
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        initObserver()
    }

    abstract fun getLayoutId(): Int
    abstract fun initData()
    abstract fun initYourView()
    abstract fun initListener()
    abstract fun initObserver()

    fun navigateToDirection(direction: NavDirections) {
        navigateTo(mBinding.root, direction)
    }

    fun navigateBack() {
        navigateUp(mBinding.root)
    }

    fun showErrorSnackbar(message: String) {
        Snackbar.make(mBinding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    fun showLoadingDialog() {
        mMainViewModel.showLoadingDialog()
    }

    fun hideLoadingDialog() {
        mMainViewModel.hideLoadingDialog()
    }
}