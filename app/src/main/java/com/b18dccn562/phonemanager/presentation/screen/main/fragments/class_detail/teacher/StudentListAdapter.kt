package com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_detail.teacher

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.b18dccn562.phonemanager.R
import com.b18dccn562.phonemanager.databinding.ItemClassRequestBinding
import com.b18dccn562.phonemanager.databinding.ItemStudentBinding
import com.b18dccn562.phonemanager.network.dto.UserDTO
import com.b18dccn562.phonemanager.presentation.screen.main.fragments.class_request.ClassRequestAdapter
import javax.inject.Inject

class StudentListAdapter @Inject constructor() :
    RecyclerView.Adapter<StudentListAdapter.StudentHolder>() {

    private var data: MutableList<UserDTO> = ArrayList()

    interface StudentClassListener {
        fun onRemoveClick(studentId: Long)
        fun onBanClick(studentId: Long)
    }

    var studentClassListener: StudentClassListener? = null

    inner class StudentHolder(private val binding: ItemStudentBinding) :
        RecyclerView.ViewHolder(binding.root), PopupMenu.OnMenuItemClickListener {

        private var itemApp: UserDTO? = null

        fun bind(item: UserDTO) {
            itemApp = item
            binding.student = item
            binding.executePendingBindings()
            binding.btnMore.setOnClickListener {
                showPopup(binding)
            }
        }

        private fun showPopup(binding: ItemStudentBinding) {
            val menu = PopupMenu(binding.root.context, binding.btnMore)
            menu.inflate(R.menu.student_class_menu)
            menu.setOnMenuItemClickListener(this)
            menu.show()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            return when (item?.itemId) {
                R.id.btn_remove -> {
                    studentClassListener?.onRemoveClick(itemApp!!.id)
                    true
                }
                R.id.btn_ban -> {
                    studentClassListener?.onBanClick(itemApp!!.id)
                    true
                }
                else -> {
                    false
                }
            }
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