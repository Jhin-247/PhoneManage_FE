package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.parent_management

import android.app.AlertDialog
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Constants
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentParentManagementBinding
import com.b18dccn562.phonemanager.network.dto.UserDTO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ParentManagementFragment : BaseFragment<FragmentParentManagementBinding>(),
    ChildListAdapter.ViewChildUsageClick {

    private val mParentManagementViewModel by viewModels<ParentManagementViewModel>()

    @Inject
    lateinit var childListAdapter: ChildListAdapter

    private var havePartner = false

    private lateinit var myPartner: UserDTO

    override fun getLayoutId(): Int = R.layout.fragment_parent_management

    override fun initData() {
        childListAdapter.childClickListener = this
        mParentManagementViewModel.loadPartner()
        mParentManagementViewModel.loadChildren()
    }

    override fun initYourView() = with(mBinding) {
        rcvKids.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcvKids.adapter = childListAdapter

        mBinding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initListener() = with(mBinding) {
        btnAdd.setOnClickListener {
            if (!havePartner) {
                val searchDialog = AlertDialog.Builder(requireContext())
                searchDialog.setTitle("Request add parent or child")
                searchDialog.setPositiveButton(
                    "Parent"
                ) { dialog, _ ->
                    val directions =
                        ParentManagementFragmentDirections.actionParentManagementFragmentToSearchPartnerFragment()
                    navigateToDirection(directions)
                    dialog.dismiss()
                }
                searchDialog.setNegativeButton(
                    "Child"
                ) { dialog, _ ->
                    val directions =
                        ParentManagementFragmentDirections.actionParentManagementFragmentToSearchChildrenFragment()
                    navigateToDirection(directions)
                    dialog.dismiss()
                }
                searchDialog.show()
            } else {
                val directions =
                    ParentManagementFragmentDirections.actionParentManagementFragmentToSearchChildrenFragment()
                navigateToDirection(directions)
            }
        }
        mBinding.rltPartner.setOnClickListener {
            val direction =
                ParentManagementFragmentDirections.actionParentManagementFragmentToUserReportFragment(
                    myPartner
                )
            navigateToDirection(direction)
        }
    }

    override fun initObserver() {
        mParentManagementViewModel.partnerState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    mBinding.rltPartner.visibility = View.GONE
                    mBinding.tvEmptyPartner.visibility = View.VISIBLE
                    havePartner = false
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    mBinding.rltPartner.visibility = View.GONE
                    mBinding.tvEmptyPartner.visibility = View.VISIBLE
                    havePartner = false
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == Constants.ResponseCode.SUCCESS) {
                        mBinding.partner = it.data.data!!
                        mBinding.rltPartner.visibility = View.VISIBLE
                        mBinding.tvEmptyPartner.visibility = View.GONE
                        havePartner = true
                        myPartner = it.data.data!!
                    } else {
                        mBinding.rltPartner.visibility = View.GONE
                        mBinding.tvEmptyPartner.visibility = View.VISIBLE
                        havePartner = false
                    }
                }
            }
        }

        mParentManagementViewModel.childrenState.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    mBinding.tvEmptyKid.visibility = View.GONE
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == Constants.ResponseCode.SUCCESS) {
                        childListAdapter.submitList(it.data.data!!)
                        mBinding.tvEmptyKid.visibility = View.GONE
                    } else {
                        childListAdapter.submitList(listOf())
                        mBinding.tvEmptyKid.visibility = View.VISIBLE
                    }
                }
            }
        }


    }

    override fun onViewChildUsageClick(user: UserDTO) {
        val direction =
            ParentManagementFragmentDirections.actionParentManagementFragmentToUserReportFragment(
                user
            )
        navigateToDirection(direction)
    }
}