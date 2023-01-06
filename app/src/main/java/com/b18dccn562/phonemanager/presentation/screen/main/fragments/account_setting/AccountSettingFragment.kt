package com.b18dccn562.phonemanager.presentation.screen.main.fragments.account_setting

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentAccountSettingBinding
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.utils.getUserAvatarImage
import com.b18dccn562.phonemanager.utils.loadImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountSettingFragment : BaseFragment<FragmentAccountSettingBinding>() {

    private val mAccountSettingViewModel by viewModels<AccountSettingViewModel>()

    private val mRequestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                imagePicker.launch(intent)
            }
        }

    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if ((it.resultCode == Activity.RESULT_OK) && (it.data != null)) {
            val data = it.data
            val uri = data?.data!!
            mAccountSettingViewModel.setImage(uri)
            loadImage(mBinding.ivAvatar, uri)
        }
    }

    private lateinit var currentUser: UserDTO

    override fun getLayoutId(): Int = R.layout.fragment_account_setting

    override fun initData() {
        currentUser = mMainViewModel.getUserDTO()!!
        mAccountSettingViewModel.setEmail(currentUser.email)
    }

    override fun initYourView() {
        mMainViewModel.hideHeaderAndFooter()
        loadImage(mBinding.ivAvatar, getUserAvatarImage(mMainViewModel.getUserEmail()!!))
        mBinding.etFullName.setText(mMainViewModel.getUserName())
        mBinding.tvEmail.text = mMainViewModel.getUserEmail()
    }

    override fun initListener() {
        with(mBinding) {
            btnBack.setOnClickListener {
                navigateBack()
            }
            btnEdit.setOnClickListener {
                mAccountSettingViewModel.goToEditMode()
            }
            cvAvatar.setOnClickListener {
                if (requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    imagePicker.launch(intent)
                } else {
                    mRequestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }

            }
        }
    }

    override fun initObserver() {
        mAccountSettingViewModel.isInEditMode.observe(viewLifecycleOwner) {
            if(it == true){
                setupForEdit()
                loadImage(mBinding.btnEdit, R.drawable.ic_done)
            }
        }
        mAccountSettingViewModel.imageUri.observe(viewLifecycleOwner) {
            if (it == null) {
                loadImage(mBinding.ivAvatar, getUserAvatarImage(mMainViewModel.getUserEmail()!!))
            } else {
                loadImage(mBinding.ivAvatar, it)
            }
        }
        mAccountSettingViewModel.fullName.observe(viewLifecycleOwner) {
            if (it == null) {
                mBinding.etFullName.setText(mMainViewModel.getUserName())
            } else {
                mBinding.etFullName.setText(it)
            }
        }
        mAccountSettingViewModel.password.observe(viewLifecycleOwner) {
            if (it == null) {
                mBinding.etPassword.setText(mMainViewModel.getPassword())
            } else {
                mBinding.etPassword.setText(it)
            }
        }
        mAccountSettingViewModel.updatingState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    showErrorSnackbar("Something went wrong with message ${it.exception?.message}")
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    mMainViewModel.setUserDto(it.data!!.data!!)
                    navigateBack()
                }
            }
        }
    }

    private fun setupForEdit() {
            loadImage(mBinding.btnEdit, R.drawable.ic_done)
            mBinding.btnEdit.setOnClickListener {
                mAccountSettingViewModel.update()
            }
            mBinding.cvAvatar.isClickable = true
            mBinding.etDob.isClickable = true
            mBinding.etFullName.isClickable = true
            mBinding.etPassword.isClickable = true
            mBinding.etPhoneNumber.isClickable = true
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainViewModel.showHeaderAndFooter()
    }
}