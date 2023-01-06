package com.b18dccn562.phonemanager.presentation.screen.main.fragments.search_user.parent

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemParentListBinding
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.utils.getUserAvatarImage
import com.b18dccn562.phonemanager.utils.loadImage
import javax.inject.Inject

class ParentListAdapter @Inject constructor() :
    RecyclerView.Adapter<ParentListAdapter.ParentListHolder>() {

    private val data = mutableListOf<UserDTO>()

    var onPartnerClickListener: ParentClick? = null

    interface ParentClick {
        fun onAddPartnerClick(partnerEmail: String)
        fun onViewPartnerClick(partnerEmail: String)
    }

    inner class ParentListHolder(val binding: ItemParentListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bindView(item: UserDTO) {
            binding.user = item
            binding.executePendingBindings()
            loadImage(binding.ivAvatar, getUserAvatarImage(item.email))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentListHolder {
        return ParentListHolder(
            ItemParentListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ParentListHolder, position: Int) {
        holder.bindView(data[position])
        holder.binding.btnAdd.setOnClickListener {
            onPartnerClickListener?.onAddPartnerClick(data[position].email)
        }
//        holder.binding.ivUserAvatar.setOnClickListener {
//            onPartnerClickListener?.onViewPartnerClick(data[position].email)
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