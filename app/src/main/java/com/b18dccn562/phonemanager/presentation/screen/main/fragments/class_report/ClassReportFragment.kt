package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_report

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.base.BaseFragment
import com.b18dccn562.phonemanager.common.Resource
import com.b18dccn562.phonemanager.databinding.FragmentClassReportBinding
import com.b18dccn562.phonemanager.network.dto.AppUsageDTO
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import com.b18dccn562.phonemanager.network.dto.ReportDTO
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.user_report.UserReport
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.user_report.getAppIcon
import com.google.firebase.database.core.Repo
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ClassReportFragment : BaseFragment<FragmentClassReportBinding>() {

    private val mReportViewModel by viewModels<ClassReportViewModel>()

    override fun getLayoutId(): Int = R.layout.fragment_class_report

    @Inject
    lateinit var classReportAdapter: ClassReportAdapter

    private lateinit var classInfo: ClassDTO

    override fun initData() {
        retrieveData()
        mReportViewModel.loadAppUsage(classInfo.id)
    }

    private fun retrieveData() {
        val bundle = arguments ?: return
        val args = ClassReportFragmentArgs.fromBundle(bundle)
        classInfo = args.classInfo
        mBinding.classReport = classInfo
    }

    override fun initYourView() {
        mMainViewModel.hideHeaderAndFooter()
        mBinding.rcvReports.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        mBinding.rcvReports.adapter = classReportAdapter
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
                    classReportAdapter.submitList(listOf())
                }
                is Resource.Error -> {
                    hideLoadingDialog()
                    classReportAdapter.submitList(listOf())
                }
                is Resource.Loading -> {
                    showLoadingDialog()
                    classReportAdapter.submitList(listOf())
                }
                is Resource.Success -> {
                    hideLoadingDialog()
                    if (it.data!!.code == 200) {
                        processDataAndShow(it.data.data!!)
                    } else {
                        classReportAdapter.submitList(listOf())
                    }
                }
            }
        }
    }

    private fun processDataAndShow(data: List<ReportDTO>) {
        val listToShow = mutableListOf<ClassReport>()
        for (i in 0 until data.size - 1) {
            if (i == data.size) {
                break
            }
            if (data[i].appUsage!!.app!!.packageName != data[i + 1].appUsage!!.app!!.packageName) {
                continue
            }
            val userReport = ClassReport()
            userReport.appName = data[i].appUsage!!.app!!.appName
            userReport.appIcon = getAppIcon(data[i].appUsage!!.app!!.packageName)
            val startTime = data[i].time
            val startDate = Date(startTime)
            val format = SimpleDateFormat("dd.MM HH:mm:ss", Locale.getDefault())
            val startTimeAsString = format.format(startDate)
            val endTime = data[i + 1].time
            val endDate = Date(endTime)
            val endTimeAsString = format.format(endDate)
            userReport.time = "$startTimeAsString - $endTimeAsString"
            userReport.studentName = data[i].appUsage!!.user!!.username
            Log.d("OkHttp", "Data: ${userReport.time}")
            listToShow.add(userReport)
        }
        classReportAdapter.submitList(listToShow)
    }
}