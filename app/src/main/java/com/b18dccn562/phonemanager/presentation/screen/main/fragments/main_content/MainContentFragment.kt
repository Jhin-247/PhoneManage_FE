package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content

import android.app.ActionBar.LayoutParams
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentMainContentBinding
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management.ClassManagementViewModel
import com.b18dccn562.phonemanager.service.AppService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainContentFragment : BaseFragment<FragmentMainContentBinding>(),
    PopupMenu.OnMenuItemClickListener {

    private lateinit var mNavController: NavController

    private val mClassViewModel by activityViewModels<ClassManagementViewModel>()

    private var currentClasses = mutableListOf<ClassDTO>()

    override fun getLayoutId(): Int = R.layout.fragment_main_content

    override fun initData() {
        if (mMainViewModel.getUserRole() == Constants.Role.CHILD) {
            mClassViewModel.loadMyStudentClass(mMainViewModel.getUserEmail()!!)
        }
    }

    override fun initYourView() {
        setupView()
        setupBottomNavigation()
        startAppService()
    }

    private fun startAppService() {
        requireActivity().startService(Intent(requireActivity(), AppService::class.java))
    }

    private fun setupView() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.user = mMainViewModel.getUserDTO()
    }

    private fun setupBottomNavigation() {
        mBinding.bottomNavigation.menu.clear()
        if (mMainViewModel.getUserRole() == Constants.Role.TEACHER) {
            mBinding.bottomNavigation.inflateMenu(R.menu.bottom_menu_teacher)
        } else if (mMainViewModel.getUserRole() == Constants.Role.PARENT) {
            mBinding.bottomNavigation.inflateMenu(R.menu.bottom_menu_parent)
        } else {
            mBinding.bottomNavigation.inflateMenu(R.menu.bottom_menu_kid)
        }

        val navHost =
            childFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        mNavController = navHost.navController
        mBinding.bottomNavigation.setupWithNavController(mNavController)
    }

    override fun initListener() {
        mBinding.btnMore.setOnClickListener {
            showPopup()
        }
    }

    private fun showPopup() {
        val menu = PopupMenu(context, mBinding.btnMore)
        menu.inflate(R.menu.notification_request_menu)
        menu.setOnMenuItemClickListener(this)
        menu.show()
    }

    override fun initObserver() {
        mMainViewModel.showHeaderAndFooter.observe(viewLifecycleOwner) {
            if (it == false) {
                mBinding.rltHeader.visibility = View.GONE
                mBinding.bottomNavigation.visibility = View.GONE
                val param = mBinding.fragmentContainerView.layoutParams
                param.height = LayoutParams.MATCH_PARENT
                mBinding.fragmentContainerView.layoutParams = param
            } else {
                mBinding.rltHeader.visibility = View.VISIBLE
                mBinding.bottomNavigation.visibility = View.VISIBLE
                val param = mBinding.fragmentContainerView.layoutParams
                param.height = 0
                mBinding.fragmentContainerView.layoutParams = param
            }
        }

        if (mMainViewModel.getUserRole() == Constants.Role.CHILD) {
            mClassViewModel.loadKidClassState.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Empty -> {

                    }
                    is Resource.Error -> {

                    }
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {

                    }
                }
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.btn_notification -> {
                val direction =
                    MainContentFragmentDirections.actionMainContentFragmentToNotificationFragment()
                navigateToDirection(direction)
                true
            }
            R.id.request -> {
                val direction =
                    MainContentFragmentDirections.actionMainContentFragmentToRequestFragment()
                navigateToDirection(direction)
                true
            }
            else -> {
                false
            }
        }
    }
}