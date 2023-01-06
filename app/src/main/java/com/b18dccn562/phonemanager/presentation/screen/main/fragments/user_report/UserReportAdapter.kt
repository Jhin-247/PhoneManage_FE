package com.b18dccn562.phonemanager.presentation.screen.main.fragments.user_report

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemStudentBinding
import com.b18dccn562.phonemanager.databinding.ItemUserReportBinding
import com.b18dccn562.phonemanager.network.dto.UserDTO
import javax.inject.Inject

class UserReportAdapter @Inject constructor(

) : RecyclerView.Adapter<UserReportAdapter.UserReportHolder>() {

    private var data: MutableList<UserReport> = ArrayList()

    inner class UserReportHolder(private val binding: ItemUserReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var itemApp: UserReport? = null

        fun bind(item: UserReport) {
            itemApp = item
            binding.usage = item
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserReportHolder {
        return UserReportHolder(
            ItemUserReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: UserReportHolder, position: Int) {
        //TODO need improve performance
        val itemToBind = data[position]
        holder.bind(itemToBind)
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<UserReport>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}