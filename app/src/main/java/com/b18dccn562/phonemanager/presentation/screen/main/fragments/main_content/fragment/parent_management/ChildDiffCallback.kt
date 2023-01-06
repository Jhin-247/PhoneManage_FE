package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.parent_management

import androidx.recyclerview.widget.DiffUtil
import com.b18dccn562.phonemanager.network.dto.UserDTO

class ChildDiffCallback constructor(
    private val oldList: List<UserDTO>,
    private val newList: List<UserDTO>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].email == newList[newItemPosition].email
    }
}