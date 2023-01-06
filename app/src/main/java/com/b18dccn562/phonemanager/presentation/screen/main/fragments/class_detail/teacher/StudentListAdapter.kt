package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_detail.teacher

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemStudentBinding
import com.b18dccn562.phonemanager.network.dto.UserDTO
import javax.inject.Inject

class StudentListAdapter @Inject constructor() :
    RecyclerView.Adapter<StudentListAdapter.StudentHolder>() {

    private var data: MutableList<UserDTO> = ArrayList()

    inner class StudentHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var itemApp: UserDTO? = null

        fun bind(item: UserDTO) {
            itemApp = item
            binding.student = item
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentHolder {
        return StudentHolder(
            ItemStudentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: StudentHolder, position: Int) {
        //TODO need improve performance
        val itemToBind = data[position]
        holder.bind(itemToBind)
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<UserDTO>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}