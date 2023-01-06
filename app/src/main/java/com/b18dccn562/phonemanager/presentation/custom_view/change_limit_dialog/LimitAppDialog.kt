package com.b18dccn562.phonemanager.presentation.custom_view.change_limit_dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.b18dccn562.phonemanager.databinding.DialogLimitBinding
import com.b18dccn562.phonemanager.local_database.room.local_model.ItemApp

class LimitAppDialog(
    context: Context,
    var hourLimited: Int = 0,
    var minuteLimited: Int = 0,
    var app: ItemApp? = null
) : Dialog(context) {

    var remoteLimitClick: RemoteLimitClick? = null

    private lateinit var mBinding: DialogLimitBinding

    interface RemoteLimitClick {
        fun onRemoteLimitAppClick(hour: Int, minute: Int, app: ItemApp?)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mBinding = DialogLimitBinding.inflate(LayoutInflater.from(context))
        setContentView(mBinding.root)

        setupView()
        setupListener()
    }

    private fun setupView() {
        with(mBinding) {
            etHour.setText(hourLimited.toString())
            etMinute.setText(minuteLimited.toString())
        }
    }

    private fun setupListener() {
        with(mBinding) {
            btnCancel.setOnClickListener {
                dismiss()
            }
            btnAgree.setOnClickListener {
                hourLimited = 0
                minuteLimited = 0
                remoteLimitClick?.onRemoteLimitAppClick(
                    etHour.text.toString().toInt(),
                    etMinute.text.toString().toInt(),
                    app
                )
            }
        }
    }

}