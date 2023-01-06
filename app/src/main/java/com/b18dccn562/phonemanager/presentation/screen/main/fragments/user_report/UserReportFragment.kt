package com.b18dccn562.phonemanager.presentation.screen.main.fragments.user_report

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentUserReportBinding
import com.b18dccn562.phonemanager.network.dto.AppUsageDTO
import com.b18dccn562.phonemanager.network.dto.UserDTO
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UserReportFragment : BaseFragment<FragmentUserReportBinding>() {

    private val mReportViewModel by viewModels<UserReportViewModel>()

    @Inject
    lateinit var userReportAdapter: UserReportAdapter

    private lateinit var user: UserDTO

    override fun getLayoutId(): Int = R.layout.fragment_user_report

    override fun initData() {
        retrieveData()
        mReportViewModel.loadAppUsage(user.email)
    }

    private fun retrieveData() {
        val bundle = arguments ?: return
        val args = UserReportFragmentArgs.fromBundle(bundle)
        user = args.userInfo
        mBinding.user = user
    }

    override fun initYourView() {
        mMainViewModel.hideHeaderAndFooter()
        mBinding.rcvReports.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.rcvReports.adapter = userReportAdapter
        mBinding.lifecycleOwner = viewLifecycleOwner
    }

    override fun initListener() {
        mBinding.btnBack.setOnClickListener {
            navigateBack()
        }
    }

    override fun initObserver() {
        mReportViewModel.loadAppUsageStatus.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Empty -> {
                    hideLoadingDialog()
                    userReportAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    userReportAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                    userReportAdapter.submitList(listOf())
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        processDataAndShow(it.data.data!!)
                    } else {
                        userReportAdapter.submitList(listOf())
                    }
                }
            }
        }
    }

    private fun processDataAndShow(data: List<AppUsageDTO>) {
        val listToShow = mutableListOf<UserReport>()
        for (i in 0 until data.size - 1) {
            if (i == data.size) {
                break
            }
            if (data[i].app!!.packageName != data[i + 1].app!!.packageName) {
                continue
            }
            val userReport = UserReport()
            userReport.appName = data[i].app!!.appName
            userReport.appIcon = getAppIcon(data[i].app!!.packageName)
            val startTime = data[i].time
            val startDate = Date(startTime)
            val format = SimpleDateFormat("dd.MM HH:mm:ss", Locale.getDefault())
            val startTimeAsString = format.format(startDate)
            val endTime = data[i + 1].time
            val endDate = Date(endTime)
            val endTimeAsString = format.format(endDate)
            userReport.appTime = "$startTimeAsString - $endTimeAsString"
            Log.d("OkHttp", "Data: ${userReport.appTime}")
            listToShow.add(userReport)
        }
        userReportAdapter.submitList(listToShow)
    }

    override fun onDestroy() {
        super.onDestroy()
        mMainViewModel.showHeaderAndFooter()
    }

//    private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
//        val formattedDateList = ArrayList<String>()
//
//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 0)
//        calendar.set(Calendar.MINUTE, 0)
//        calendar.set(Calendar.SECOND, 0)
//        for (i in 0..7) {
//            val currentTime = calendar.time
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//            formattedDateList.add(dateFormat.format(currentTime))
//            calendar.add(Calendar.DAY_OF_YEAR, 1)
//        }
//        val date = calendar.time
//        date.time
//        print("------${date.time}")
//
//        return formattedDateList
//    }
}