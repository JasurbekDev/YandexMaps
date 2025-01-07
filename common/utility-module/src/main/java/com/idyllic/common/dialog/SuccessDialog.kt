package com.idyllic.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.idyllic.ui_module.databinding.DialogSuccessBinding

class SuccessDialog(
    context: Context,
    private var title: String = "",
    private var massage: String,
    private var isCancelable: Boolean = false,
    private val listener: (Dialog) -> Unit
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogSuccessBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(isCancelable)
        binding.textTitle.text = title
        binding.textMessage.text = massage
        binding.cardClose.setOnClickListener {
            listener.invoke(this)
        }
    }
}