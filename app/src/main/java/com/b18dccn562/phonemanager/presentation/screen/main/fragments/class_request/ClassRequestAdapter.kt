package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_request

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.databinding.ItemClassRequestBinding
import com.b18dccn562.phonemanager.network.dto.ClassRequestDTO
import javax.inject.Inject

class ClassRequestAdapter @Inject constructor() :
    RecyclerView.Adapter<ClassRequestAdapter.ClassRequestHolder>() {

    private var data: MutableList<ClassRequestDTO> = ArrayList()

    interface RequestClickListener {
        fun onAcceptClick(requestId: Long)
        fun onDenyClick(requestId: Long)
    }

    var requestClickListener: RequestClickListener? = null

    inner class ClassRequestHolder(private val binding: ItemClassRequestBinding) :
        RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {
        private var itemApp: ClassRequestDTO? = null

        fun bind(item: ClassRequestDTO) {
            itemApp = item
            binding.request = item
            binding.executePendingBindings()
            binding.btnMore.setOnClickListener {
                showPopup(binding)
            }
        }

        private fun showPopup(binding: ItemClassRequestBinding) {
            val menu = PopupMenu(binding.root.context, binding.btnMore)
            menu.inflate(R.menu.teacher_request_click)
            menu.setOnMenuItemClickListener(this)
            menu.show()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.btn_accept -> {
                    requestClickListener?.onAcceptClick(itemApp!!.id)
                    true
                }
                R.id.btn_limit -> {
                    requestClickListener?.onDenyClick(itemApp!!.id)
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassRequestHolder {
        return ClassRequestHolder(
            ItemClassRequestBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ClassRequestHolder, position: Int) {
        val itemToBind = data[position]
        holder.bind(itemToBind)
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<ClassRequestDTO>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}