package com.b18dccn562.phonemanager.presentation.screen.main

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.databinding.ActivityMainBinding
import com.b18dccn562.phonemanager.presentation.custom_view.loading_dialog.LoadingDialog
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management.ClassManagementViewModel
import com.b18dccn562.phonemanager.utils.signUpNewAccountVariables
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var mNavHostFragment: NavHostFragment
    private var mNavController: NavController? = null
    private lateinit var mBinding: ActivityMainBinding

    private val mMainViewModel by viewModels<MainViewModel>()

    private lateinit var mLoadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        initData()
        initView()
        setupNavigation()
        setupObserver()
    }

    private fun initData() {
        mLoadingDialog = LoadingDialog(this)
    }

    private fun setupObserver() {
        mMainViewModel.showLoadingDialogState.observe(this) {
            if (it == true) {
                showLoadingDialog()
            } else {
                hideLoadingDialog()
            }
        }
        mMainViewModel.logout.observe(this) {
            if(it == true){
                val intent = intent
                finish()
                startActivity(intent)
//                mNavController?.navigate(R.id.action_global_signInFragment)
//                mMainViewModel.setLogoutComplete()
            }
        }
    }

    private fun hideLoadingDialog() {
        mLoadingDialog.dismiss()
    }

    private fun showLoadingDialog() {
        mLoadingDialog.show()
    }

    private fun setupNavigation() {
        mNavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        mNavHostFragment.let {
            mNavController = mNavHostFragment.navController
        }
    }

    private fun initView() {
        hideSupportActionBar()
        setStatusBarColor(R.color.main_color)
    }

    private fun hideSupportActionBar() {
        supportActionBar?.let {
            it.hide()
        }
    }

    private fun setStatusBarColor(color: Int) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor =
            ContextCompat.getColor(applicationContext, color)
    }
}