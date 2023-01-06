package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.all_app.fragment

import androidx.recyclerview.widget.DiffUtil
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp

class AppDiffUtilCallBack(
    private val oldList: List<ItemApp>,
    private val newList: List<ItemApp>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].packageName == newList[newItemPosition].packageName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        if (oldList[oldItemPosition].isLock != newList[newItemPosition].isLock) {
            return false
        }
        if (oldList[oldItemPosition].timeLimited != newList[newItemPosition].timeLimited) {
            return false
        }
        if (oldList[oldItemPosition].timeUsedInDay != newList[newItemPosition].timeUsedInDay) {
            return false
        }
        if (oldList[oldItemPosition].packageName != newList[newItemPosition].packageName) {
            return false
        }
        if (oldList[oldItemPosition].appName != newList[newItemPosition].appName) {
            return false
        }
        return true
    }

}