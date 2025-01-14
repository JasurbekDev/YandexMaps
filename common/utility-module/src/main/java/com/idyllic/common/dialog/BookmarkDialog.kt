package com.idyllic.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.idyllic.ui_module.databinding.DialogBookmarkBinding

class BookmarkDialog(
    private val context: Context,
    private val massage: String,
    private val yesListener: (Dialog) -> Unit,
    private val noListener: (Dialog) -> Unit
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogBookmarkBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        binding.textName.text = massage
        binding.btnConfirm.setOnClickListener {
            yesListener.invoke(this)
        }
        binding.btnCancel.setOnClickListener {
            noListener.invoke(this)
        }
    }
}