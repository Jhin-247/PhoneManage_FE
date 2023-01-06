package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.databinding.ItemAppUsageBinding
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.AppDiffUtilCallBack
import com.b18dccn562.phonemanager.utils.AppUtils
import com.b18dccn562.phonemanager.utils.enum_data.AppQueryEnum
import com.b18dccn562.phonemanager.utils.getPercentageUsage
import javax.inject.Inject

class AppUsageAdapter @Inject constructor(
    private val appUtils: AppUtils
) :
    RecyclerView.Adapter<AppUsageAdapter.AppUsageViewHolder>() {
    private var data: MutableList<ItemApp> = ArrayList()

    var queryType: AppQueryEnum = AppQueryEnum.ONE_DAY

    inner class AppUsageViewHolder(private val binding: ItemAppUsageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ItemApp) {
            binding.app = item
            binding.executePendingBindings()
            val percentage = getPercentageUsage(itemApp = item, queryType)
            binding.tvAppUsage.text =
                String.format(
                    binding.root.context.getString(R.string.usage_percentage),
                    percentage
                )
            binding.progressPercentage.progress = (percentage * 100).toInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppUsageViewHolder {
        return AppUsageViewHolder(
            ItemAppUsageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AppUsageViewHolder, position: Int) {
        val itemToBind = data[position]
        itemToBind.appIcon = appUtils.getAppIconByAppName(itemToBind.packageName)
        holder.bind(itemToBind)
    }

    override fun getItemCount(): Int = data.size

    fun submitList(data: List<ItemApp>) {
        val diffResult = DiffUtil.calculateDiff(AppDiffUtilCallBack(this.data, data))
        this.data.clear()
        this.data.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }
}