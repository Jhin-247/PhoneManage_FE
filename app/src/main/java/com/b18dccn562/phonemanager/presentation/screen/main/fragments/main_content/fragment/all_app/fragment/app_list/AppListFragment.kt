package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.app_list

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentAppListBinding
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.presentation.custom_view.change_limit_dialog.LimitAppDialog
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.AppViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppListFragment : BaseFragment<FragmentAppListBinding>(),
    AppListAdapter.OnAppMoreClickListener, LimitAppDialog.RemoteLimitClick {

    @Inject
    lateinit var appsAdapter: AppListAdapter

    private val mAppViewModel by activityViewModels<AppViewModel>()

    private lateinit var limitedDialog: LimitAppDialog

    override fun getLayoutId(): Int = R.layout.fragment_app_list

    override fun initData() {
        appsAdapter.moreClickListener = this
        mAppViewModel.loadAllApps()

        limitedDialog = LimitAppDialog(requireContext())
        limitedDialog.remoteLimitClick = this
    }

    override fun initYourView() = with(mBinding) {
        rcvApps.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcvApps.adapter = appsAdapter
    }

    override fun initListener() {
    }

    override fun initObserver() {
        mAppViewModel.allAppFromDevice.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                }
                is Resource.Error -> {
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    appsAdapter.submitList(it.data!!)
                }
            }
        }
        mAppViewModel.isInitializing.observe(viewLifecycleOwner) {
            if (it == true) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }
    }

    override fun onLockOrUnLockClick(itemApp: ItemApp) {
        mAppViewModel.lockOrUnLockApp(itemApp)
    }

    override fun onLimitClick(itemApp: ItemApp) {
        limitedDialog.minuteLimited = 0
        limitedDialog.hourLimited = 0
        limitedDialog.app = itemApp
        limitedDialog.show()
    }

    override fun onRemoteLimitAppClick(hour: Int, minute: Int, app: ItemApp?) {
        if(hour > 24){
            showErrorSnackbar("hour too big")
        }
        if(minute > 60){
            showErrorSnackbar("minute too big")
        }
        val time = ((hour * 60 + minute) * 60000).toLong()
        mAppViewModel.changeAppLimit(app!!, time)
        limitedDialog.dismiss()
    }


}