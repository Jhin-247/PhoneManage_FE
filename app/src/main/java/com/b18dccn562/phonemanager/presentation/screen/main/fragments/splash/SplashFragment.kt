package com.b18dccn562.phonemanager.presentation.screen.main.fragments.splash

import android.view.animation.AnimationUtils
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentSplashBinding
import com.b18dccn562.phonemanager.local_database.shared_preference.AccountPreference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val splashJob = Job()

    private var hasAccount = false

    @Inject
    lateinit var accountPreference: AccountPreference

    override fun getLayoutId(): Int = R.layout.fragment_splash

    override fun initData() {
    }

    override fun initYourView() {
        val logoAnimSlide = AnimationUtils.loadAnimation(context, R.anim.slide_in_left_to_right)
        mBinding.ivLogo.startAnimation(logoAnimSlide)

        val textAnimSlide = AnimationUtils.loadAnimation(context, R.anim.slide_in_right_to_left)
        mBinding.tvAppName.startAnimation(textAnimSlide)

        checkExistedAccountAndLogin()
    }

    private fun checkExistedAccountAndLogin() {
        if (accountPreference.hasAccount()) {
            val email = accountPreference.getEmail()
            val password = accountPreference.getPassword()
            mMainViewModel.doLogin(email, password)
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        CoroutineScope(Dispatchers.Main + splashJob).launch {
            delay(3000)
            if (!hasAccount) {
                val direction = SplashFragmentDirections.actionSplashFragmentToSignInFragment()
                navigateToDirection(direction)
            } else {
                val direction = SplashFragmentDirections.actionSplashFragmentToChangeUserInformationFragment()
                navigateToDirection(
                    direction
                )
            }

        }
    }

    override fun initListener() {
    }

    override fun initObserver() {
        mMainViewModel.loginState.observe(this) {
            when (it) {
                is Resource.Error -> {
                    startTimer()
                }
                is Resource.Loading -> {
                }
                is Resource.Success -> {
                    if(it.data!!.code == 200) {
                        hasAccount = true
                    }
                    startTimer()
                }
                else -> {
                    startTimer()
                }
            }
        }
    }

}