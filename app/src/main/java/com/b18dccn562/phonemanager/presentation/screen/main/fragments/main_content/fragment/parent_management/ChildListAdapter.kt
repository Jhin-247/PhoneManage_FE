package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.parent_management

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemChildBinding
import com.b18dccn562.phonemanager.network.dto.UserDTO
import javax.inject.Inject

class ChildListAdapter @Inject constructor(

) : RecyclerView.Adapter<ChildListAdapter.ChildHolder>() {

    private val data = mutableListOf<UserDTO>()

    var childClickListener: ViewChildUsageClick? = null

    interface ViewChildUsageClick {
        fun onViewChildUsageClick(user: UserDTO)
    }

    inner class ChildHolder(private val binding: ItemChildBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindView(item: UserDTO) {
            binding.child = item
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                childClickListener?.onViewChildUsageClick(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildHolder {
        return ChildHolder(
            ItemChildBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChildHolder, position: Int) {
        holder.bindView(data[position])
    }

    override fun getItemCount(): Int = data.size

    fun submitList(data: List<UserDTO>) {
        val diffResult = DiffUtil.calculateDiff(ChildDiffCallback(this.data, data))
        this.data.clear()
        this.data.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

}