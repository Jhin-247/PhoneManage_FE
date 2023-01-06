package com.b18dccn562.phonemanager.presentation.screen.main.fragments.search_user.parent

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentSearchPartnerBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class SearchPartnerFragment : BaseFragment<FragmentSearchPartnerBinding>(),
    ParentListAdapter.ParentClick {

    private val mSearchPartnerViewModel by viewModels<SearchPartnerViewModel>()

    @Inject
    lateinit var parentListAdapter: ParentListAdapter

    override fun getLayoutId(): Int = R.layout.fragment_search_partner

    override fun initData() {
        val user = mMainViewModel.getUserDTO()!!
        mSearchPartnerViewModel.setUser(user)

        parentListAdapter.onPartnerClickListener = this
    }

    override fun initYourView() = with(mBinding) {
        partner = mSearchPartnerViewModel
        lifecycleOwner = viewLifecycleOwner

        rcvParent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rcvParent.adapter = parentListAdapter

        mMainViewModel.hideHeaderAndFooter()
    }

    override fun initListener() {
        mBinding.btnSearch.setOnClickListener {
//            hideKeyboard()
            mSearchPartnerViewModel.search()
        }
        mBinding.btnBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun initObserver() {
        mSearchPartnerViewModel.searchStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    parentListAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    parentListAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        parentListAdapter.submitList(it.data.data!!)
                    } else {
                        parentListAdapter.submitList(listOf())
                    }
                }
            }
        }
        mSearchPartnerViewModel.addPartnerRequest.observe(viewLifecycleOwner){
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        navigateBack()
                    }
                }
            }
        }

    }

    override fun onAddPartnerClick(partnerEmail: String) {
        mSearchPartnerViewModel.sendPartnerRequest(partnerEmail)
    }

    override fun onViewPartnerClick(partnerEmail: String) {

    }

    override fun onDestroy() {
        super.onDestroy()
        mMainViewModel.showHeaderAndFooter()
    }
}