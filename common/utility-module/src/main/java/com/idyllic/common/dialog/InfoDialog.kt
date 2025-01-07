package com.idyllic.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.idyllic.ui_module.databinding.DialogInfoBinding

class InfoDialog(
    private val context: Context,
    private val massage: String,
    private val listener: (Dialog) -> Unit
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogInfoBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        binding.textMessage.text = massage
        binding.cardClose.setOnClickListener {
            listener.invoke(this)
        }
    }
}