package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_report

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.databinding.ItemLiveReportBinding
import javax.inject.Inject

class LiveStudentReportAdapter @Inject constructor(

) : RecyclerView.Adapter<LiveStudentReportAdapter.ClassReportHolder>() {

    private var data: MutableList<LiveStudentReport> = ArrayList()

    inner class ClassReportHolder(private val binding: ItemLiveReportBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var itemApp: LiveStudentReport? = null

        fun bind(item: LiveStudentReport) {
            itemApp = item
            binding.usage = item
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassReportHolder {
        return ClassReportHolder(
            ItemLiveReportBinding.inflate(
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
    fun submitList(data: List<LiveStudentReport>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }
}