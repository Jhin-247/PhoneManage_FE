package com.b18dccn562.phonemanager.presentation.screen.main.fragments.notification_and_request.notification

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemNotificationBinding
import com.b18dccn562.phonemanager.network.dto.NotificationDTO
import javax.inject.Inject

class NotificationAdapter @Inject constructor(

) : RecyclerView.Adapter<NotificationAdapter.NotificationHolder>() {

    private var data: MutableList<NotificationDTO> = ArrayList()

    inner class NotificationHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var itemApp: NotificationDTO? = null

        fun bind(item: NotificationDTO) {
            itemApp = item
            binding.notification = itemApp
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationHolder {
        return NotificationHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationHolder, position: Int) {
        //TODO need improve performance
        val itemToBind = data[position]
        holder.bind(itemToBind)
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<NotificationDTO>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}