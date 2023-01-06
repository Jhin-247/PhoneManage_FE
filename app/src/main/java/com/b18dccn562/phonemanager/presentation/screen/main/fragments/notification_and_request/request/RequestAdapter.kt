package com.b18dccn562.phonemanager.presentation.screen.main.fragments.notification_and_request.request

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemRequestBinding
import com.b18dccn562.phonemanager.network.dto.RequestDTO
import javax.inject.Inject

class RequestAdapter @Inject constructor(

) : RecyclerView.Adapter<RequestAdapter.RequestHolder>() {
    private var data: MutableList<RequestDTO> = ArrayList()

    interface RequestClick {
        fun onAcceptRequest(requestDTO: RequestDTO)
        fun onDenyRequest(requestDTO: RequestDTO)
    }

    var requestListener: RequestClick? = null

    inner class RequestHolder(private val binding: ItemRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var itemApp: RequestDTO? = null

        fun bind(item: RequestDTO) {
            itemApp = item
            binding.request = itemApp
            binding.executePendingBindings()
            binding.btnAgree.setOnClickListener {
                requestListener?.onAcceptRequest(item)
            }
            binding.btnDeny.setOnClickListener {
                requestListener?.onDenyRequest(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestHolder {
        return RequestHolder(
            ItemRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RequestHolder, position: Int) {
        //TODO need improve performance
        val itemToBind = data[position]
        holder.bind(itemToBind)
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<RequestDTO>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}