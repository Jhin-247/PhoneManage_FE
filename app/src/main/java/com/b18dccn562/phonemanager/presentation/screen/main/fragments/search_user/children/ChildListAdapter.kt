package com.b18dccn562.phonemanager.presentation.screen.main.fragments.search_user.children

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemParentListBinding
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.utils.getUserAvatarImage
import com.b18dccn562.phonemanager.utils.loadImage
import javax.inject.Inject

class ChildListAdapter @Inject constructor() :
    RecyclerView.Adapter<ChildListAdapter.ChildListHolder>() {

    private val data = mutableListOf<UserDTO>()

    var onChildClickListener: ChildClick? = null

    interface ChildClick {
        fun onAddChildClick(childEmail: String)
        fun onViewChildClick(childEmail: String)
    }

    inner class ChildListHolder(val binding: ItemParentListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bindView(item: UserDTO) {
            binding.user = item
            binding.executePendingBindings()
            loadImage(binding.ivAvatar, getUserAvatarImage(item.email))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildListHolder {
        return ChildListHolder(
            ItemParentListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChildListHolder, position: Int) {
        holder.bindView(data[position])
        holder.binding.btnAdd.setOnClickListener {
            onChildClickListener?.onAddChildClick(data[position].email)
        }
//        holder.binding.ivUserAvatar.setOnClickListener {
//            onChildClickListener?.onViewChildClick(data[position].email)
//        }
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(parentList: List<UserDTO>) {
        data.clear()
        data.addAll(parentList)
        notifyDataSetChanged()
    }

}