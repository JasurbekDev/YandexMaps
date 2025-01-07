package com.idyllic.common.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.idyllic.ui_module.databinding.DialogQuestionBinding

class QuestionDialog(
    context: Context,
    private val massage: String,
    private val yesListener: (Dialog) -> Unit,
    private val closeListener: (Dialog) -> Unit
) : Dialog(context) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        val binding = DialogQuestionBinding.inflate(LayoutInflater.from(context))
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        binding.textMessageQuestion.text = massage
        binding.cardYes.setOnClickListener {
            yesListener.invoke(this)
        }
        binding.cardClose.setOnClickListener {
            closeListener.invoke(this)
        }
    }
}