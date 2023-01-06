package com.b18dccn562.phonemanager.presentation.screen.main.fragments.change_user_avatar

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.databinding.FragmentChangeUserInformationBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChangeUserInformationFragment : BaseFragment<FragmentChangeUserInformationBinding>() {

    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if ((it.resultCode == RESULT_OK) && (it.data != null)) {
            val data = it.data
            val uri = data?.data!!
//            mUserInformationViewModel.userAvatarUri = uri
//            loadImage(mBinding.ivAvatar, uri)
        }
    }

    private val mRequestPermissionLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            if (it) {
                pickImage()
            }
        }

    private val mUserInformationViewModel by activityViewModels<ChangeUserInformationViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_change_user_information

    override fun initData() {
    }

    override fun initYourView() {
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.user = mMainViewModel.getUserDTO()
//        if (mMainViewModel.user.value!!.avatarUrl != null) {
//            mBinding.btnRegister.text = getString(R.string.change_avatar)
//        }
    }

    override fun initListener() {
//        with(mBinding) {
//            cvAvatar.setOnClickListener {
//                if (requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
//                    pickImage()
//                } else {
//                    mRequestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                }
//            }
//            btnRegister.setOnClickListener {
//                if (mMainViewModel.user.value!!.avatarUrl == null) {
//                    mUserInformationViewModel.setUserValue(mMainViewModel.user.value)
//                    mUserInformationViewModel.changeUserAvatar()
//                } else {
//                    if(mUserInformationViewModel.userAvatarUri != null){
//                        mUserInformationViewModel.setUserValue(mMainViewModel.user.value)
//                        mUserInformationViewModel.changeUserAvatar()
//                    } else {
//                        val navigateToPermissionAction =
//                            ChangeUserInformationFragmentDirections.actionChangeUserInformationFragmentToPermissionFragment()
//                        navigateTo(mBinding.root, navigateToPermissionAction)
//                    }
//                }
//            }
//        }
        mBinding.btnSkip.setOnClickListener {
            val direction =
                ChangeUserInformationFragmentDirections.actionChangeUserInformationFragmentToPermissionFragment()
            navigateToDirection(direction)
        }
    }

    override fun initObserver() {
//        mMainViewModel.user.observe(viewLifecycleOwner) {
//            mBinding.tvName.text = it.username
//        }
//        mUserInformationViewModel.userUpdateState.observe(viewLifecycleOwner) {
//            if (it != null) {
//                mMainViewModel.setUser(it.data!!)
//            }
//        }
//        mUserInformationViewModel.uploadComplete.observe(viewLifecycleOwner) {
//            if (it == true) {
//                val navigateToPermissionAction =
//                    ChangeUserInformationFragmentDirections.actionChangeUserInformationFragmentToPermissionFragment()
//                navigateTo(mBinding.root, navigateToPermissionAction)
//            }
//        }
    }

    private fun pickImage() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePicker.launch(intent)
    }
}