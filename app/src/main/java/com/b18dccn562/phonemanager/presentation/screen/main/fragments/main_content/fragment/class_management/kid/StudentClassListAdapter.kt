package com.b18dccn562.phonemanager.presentation.screen.main.fragments.main_content.fragment.class_management.kid

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemClassBinding
import com.b18dccn562.phonemanager.network.dto.ClassDTO
import javax.inject.Inject

class StudentClassListAdapter @Inject constructor() :
    RecyclerView.Adapter<StudentClassListAdapter.ClassHolder>() {

    private var data: MutableList<ClassDTO> = ArrayList()

    var classClickListener: ClassClick? = null

    interface ClassClick {
        fun onClassClick(itemClass: ClassDTO)
    }

    inner class ClassHolder(private val binding: ItemClassBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var itemApp: ClassDTO? = null

        fun bind(item: ClassDTO) {
            itemApp = item
            binding.itemClass = item
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                classClickListener?.onClassClick(item)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassHolder {
        return ClassHolder(
            ItemClassBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ClassHolder, position: Int) {
        //TODO need improve performance
        val itemToBind = data[position]
        holder.bind(itemToBind)
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<ClassDTO>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}