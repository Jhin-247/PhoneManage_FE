package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.app_list

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.databinding.ItemAppBinding
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment.AppDiffUtilCallBack
import com.b18dccn562.phonemanager.utils.AppUtils
import javax.inject.Inject

class AppListAdapter @Inject constructor(
    private val appUtils: AppUtils
) : RecyclerView.Adapter<AppListAdapter.AppHolder>() {

    interface OnAppMoreClickListener {
        fun onLockOrUnLockClick(itemApp: ItemApp)
        fun onLimitClick(itemApp: ItemApp)
    }

    var moreClickListener: OnAppMoreClickListener? = null

    private var data: MutableList<ItemApp> = ArrayList()

    inner class AppHolder(private val binding: ItemAppBinding) :
        RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {

        private var itemApp: ItemApp? = null

        fun bind(item: ItemApp) {
            itemApp = item
            binding.item = item
            binding.executePendingBindings()

            binding.btnMore.setOnClickListener {
                showPopup(binding)
            }
        }

        private fun showPopup(binding: ItemAppBinding) {
            val menu = PopupMenu(binding.root.context, binding.btnMore)
            menu.inflate(R.menu.app_menu)
            menu.setOnMenuItemClickListener(this)
            menu.show()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.btn_lock -> {
                    moreClickListener?.onLockOrUnLockClick(itemApp!!)
                    true
                }
                R.id.btn_limit -> {
                    moreClickListener?.onLimitClick(itemApp!!)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppHolder {
        return AppHolder(
            ItemAppBinding.inflate(
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