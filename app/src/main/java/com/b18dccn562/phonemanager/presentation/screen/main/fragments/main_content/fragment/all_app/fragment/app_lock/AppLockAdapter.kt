package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.app_lock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemAppLockBinding
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.AppDiffUtilCallBack
import com.b18dccn562.phonemanager.utils.AppUtils
import javax.inject.Inject

class AppLockAdapter @Inject constructor(
    private val appUtils: AppUtils
) : RecyclerView.Adapter<AppLockAdapter.AppHolder>() {

    interface OnAppUnLockClickListener {
        fun unlock(item: ItemApp)
    }

    var unlockClickListener: OnAppUnLockClickListener? = null

    private var data: MutableList<ItemApp> = ArrayList()

    inner class AppHolder(private val binding: ItemAppLockBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var itemApp: ItemApp? = null

        fun bind(item: ItemApp) {
            itemApp = item
            binding.item = item
            binding.executePendingBindings()

            binding.btnUnlock.setOnClickListener {
                unlockClickListener?.unlock(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHolder {
        return AppHolder(
            ItemAppLockBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AppHolder, position: Int) {
        //TODO need improve performance
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