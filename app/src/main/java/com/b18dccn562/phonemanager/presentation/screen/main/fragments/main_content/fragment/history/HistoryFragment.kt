package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.history

import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.databinding.FragmentHistoryBinding
import com.b18dccn562.phonemanager.utils.enum_data.AppQueryEnum
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : BaseFragment<FragmentHistoryBinding>(), OnItemSelectedListener {

    @Inject
    lateinit var appUsageAdapter: AppUsageAdapter

    private val mHistoryViewModel by viewModels<HistoryViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_history

    override fun initData() {
        mHistoryViewModel.query(AppQueryEnum.ONE_DAY)
        appUsageAdapter.queryType = AppQueryEnum.ONE_DAY
    }

    override fun initYourView() {
        with(mBinding) {
            rcvApps.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            rcvApps.adapter = appUsageAdapter
        }
    }

    override fun initListener() {
        mBinding.spQueryPicker.onItemSelectedListener = this
    }

    override fun initObserver() {
        mHistoryViewModel.listAppToShow.observe(viewLifecycleOwner) {
            appUsageAdapter.submitList(it)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (position == 0) {
            mHistoryViewModel.query(AppQueryEnum.ONE_DAY)
            appUsageAdapter.queryType = AppQueryEnum.ONE_DAY
        }
        if (position == 1) {
            mHistoryViewModel.query(AppQueryEnum.SEVEN_DAY)
            appUsageAdapter.queryType = AppQueryEnum.SEVEN_DAY
        }
        if (position == 2) {
            mHistoryViewModel.query(AppQueryEnum.THIRTY_DAY)
            appUsageAdapter.queryType = AppQueryEnum.THIRTY_DAY
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        mHistoryViewModel.query(AppQueryEnum.ONE_DAY)
    }
}