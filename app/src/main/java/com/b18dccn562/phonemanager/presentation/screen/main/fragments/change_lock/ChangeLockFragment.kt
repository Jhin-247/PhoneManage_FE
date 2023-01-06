package com.b18dccn562.phonemanager.presentation.screen.main.fragments.change_lock

import androidx.fragment.app.viewModels
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.databinding.FragmentChangeLockBinding
import com.b18dccn562.phonemanager.presentation.custom_view.lock_view.LockView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangeLockFragment : BaseFragment<FragmentChangeLockBinding>() {
    private val mChangeLockViewModel by viewModels<ChangeLockViewModel>()


    override fun getLayoutId(): Int = R.layout.fragment_change_lock

    override fun initData() {
    }

    override fun initYourView() {
        mChangeLockViewModel.setLockStatus(LockPatternStatus.CREATE_PATTERN)
        mMainViewModel.hideHeaderAndFooter()
    }

    override fun initListener() {
        mBinding.lockView.setOnCompleteListener(object : LockView.OnCompleteDrawingListener {
            override fun onCompleteDrawing(patterns: List<Int>) {
                mChangeLockViewModel.checkPattern(patterns)
            }
        })
        mBinding.btnBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun initObserver() {
        mChangeLockViewModel.lockStatus.observe(viewLifecycleOwner) {
            when (it) {
                LockPatternStatus.REDRAW_PATTERN -> {
                    mBinding.lockView.clearPattern()
                    mBinding.tvStatus.text = getString(R.string.redraw_pattern)
                }
                LockPatternStatus.WRONG_PATTERN -> {
                    mBinding.lockView.clearPattern()
                    mBinding.tvStatus.text = getString(R.string.wrong_pattern)
                }
                LockPatternStatus.TOO_SHORT -> {
                    mBinding.lockView.clearPattern()
                    mBinding.tvStatus.text = getString(R.string.short_pattern)
                }
                LockPatternStatus.DRAW_TO_OPEN -> {
                    mBinding.tvStatus.text = getString(R.string.draw_to_open)
                }
                LockPatternStatus.CREATE_PATTERN -> {
                    mBinding.tvStatus.text = getString(R.string.create_pattern)
                }
                null -> {
                    mBinding.lockView.clearPattern()
                }
            }
        }
        mChangeLockViewModel.changed.observe(viewLifecycleOwner) {
            if (it == true) {
                navigateBack()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainViewModel.showHeaderAndFooter()
    }
}