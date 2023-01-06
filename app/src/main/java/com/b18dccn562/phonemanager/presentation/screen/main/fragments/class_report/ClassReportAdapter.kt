package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_report

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemClassReportBinding
import javax.inject.Inject

class ClassReportAdapter @Inject constructor(

) : RecyclerView.Adapter<ClassReportAdapter.ClassReportHolder>() {

    private var data: MutableList<ClassReport> = ArrayList()

    inner class ClassReportHolder(private val binding: ItemClassReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var itemApp: ClassReport? = null

        fun bind(item: ClassReport) {
            itemApp = item
            binding.usage = item
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassReportHolder {
        return ClassReportHolder(
            ItemClassReportBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ClassReportHolder, position: Int) {
        //TODO need improve performance
        val itemToBind = data[position]
        holder.bind(itemToBind)
    }

    override fun getItemCount(): Int = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(data: List<ClassReport>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}