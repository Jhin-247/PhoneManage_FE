package com.b18dccn562.phonemanager.presentation.screen.main.fragments.search_user.children

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentSearchUserBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchChildrenFragment : BaseFragment<FragmentSearchUserBinding>(),
    ChildListAdapter.ChildClick {

    private val mSearchChildrenViewModel by viewModels<SearchChildrenViewModel>()

    @Inject
    lateinit var searchChildrenAdapter: ChildListAdapter

    override fun getLayoutId(): Int = R.layout.fragment_search_user

    override fun initData() {
        val user = mMainViewModel.getUserDTO()!!
        mSearchChildrenViewModel.setUser(user)

        searchChildrenAdapter.onChildClickListener = this
    }

    override fun initYourView() {
        mBinding.searchViewModel = mSearchChildrenViewModel
        mBinding.lifecycleOwner = viewLifecycleOwner

        mBinding.rcvChildren.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mBinding.rcvChildren.adapter = searchChildrenAdapter

        mMainViewModel.hideHeaderAndFooter()
    }

    override fun initListener() {
        mBinding.btnBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun initObserver() {
        mSearchChildrenViewModel.searchStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    searchChildrenAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    searchChildrenAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        searchChildrenAdapter.submitList(it.data.data!!)
                    } else {
                        searchChildrenAdapter.submitList(listOf())
                    }
                }
            }
        }

        mSearchChildrenViewModel.addKidRequest.observe(viewLifecycleOwner) {
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

    override fun onAddChildClick(childEmail: String) {
        mSearchChildrenViewModel.sendAddChildRequest(childEmail)
    }

    override fun onViewChildClick(childEmail: String) {

    }

    override fun onDestroy() {
        super.onDestroy()
        mMainViewModel.showHeaderAndFooter()
    }
}