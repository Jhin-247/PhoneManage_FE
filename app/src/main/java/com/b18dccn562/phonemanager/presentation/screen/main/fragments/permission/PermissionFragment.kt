package com.b18dccn562.phonemanager.presentation.screen.main.fragments.permission

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.viewModels
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.databinding.FragmentPermissionBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PermissionFragment : BaseFragment<FragmentPermissionBinding>() {

    private val mPermissionViewModel by viewModels<PermissionViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_permission

    override fun initData() {
    }

    override fun initYourView() {
        if (Settings.canDrawOverlays(context)) {
            mPermissionViewModel.setHasOverlayPermission(true)
        } else {
            mPermissionViewModel.setHasOverlayPermission(false)
        }

        mPermissionViewModel.setHasUsagePermission(checkUsageStatPermission())

    }

    override fun initListener() {
    }

    override fun initObserver() {
        mPermissionViewModel.hasAllPermission.observe(viewLifecycleOwner) {
            if (it == 2) {
//                requireActivity().startService(Intent(requireActivity(), AppService::class.java))
                val direction =
                    PermissionFragmentDirections.actionPermissionFragmentToMainContentFragment()
                navigateToDirection(direction)
            }
        }
        mPermissionViewModel.hasOverlayPermission.observe(viewLifecycleOwner) {
            if (it == true) {
                mBinding.btnGrandAppOverlayPermission.text = getString(R.string.allowed)
                mBinding.btnGrandAppOverlayPermission.isEnabled = false
                mBinding.btnGrandAppOverlayPermission.isClickable = false
            } else {
                mBinding.btnGrandAppOverlayPermission.setOnClickListener {
                    val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                    startActivity(myIntent)
                }
            }
        }
        mPermissionViewModel.hasUsagePermission.observe(viewLifecycleOwner) {
            if (it == true) {
                mBinding.btnGrandAppUsagePermission.text = getString(R.string.allowed)
                mBinding.btnGrandAppUsagePermission.isEnabled = false
                mBinding.btnGrandAppUsagePermission.isClickable = false
            } else {
                mBinding.btnGrandAppUsagePermission.setOnClickListener {
                    startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initYourView()
    }

    @Suppress("DEPRECATION")
    fun checkUsageStatPermission(): Boolean {
        val appOsManager =
            (requireActivity().applicationContext.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager)
        val mode: Int = if (Build.VERSION.SDK_INT >= 29) {
            appOsManager.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                requireActivity().packageName
            )
        } else {
            appOsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                requireActivity().packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }
}