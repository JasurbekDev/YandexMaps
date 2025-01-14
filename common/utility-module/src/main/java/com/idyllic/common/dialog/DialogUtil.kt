package com.idyllic.common.dialog

import android.app.Dialog
import android.content.Context
import androidx.fragment.app.Fragment
import com.idyllic.ui_module.R

object DialogUtil {
    fun info(context: Context, massage: String, listener: (Dialog) -> Unit): Dialog {
        return InfoDialog(context, massage, listener)
    }

    fun input(
        context: Context, title: String, listener: (Dialog, inputText: String) -> Unit
    ): Dialog {
        return EditTextDialog(context, title, listener)
    }

    fun inProcessDialog(context: Context, listener: (Dialog) -> Unit = {}): Dialog {
        return info(context, context.getString(R.string.lbl_feature_in_development)) {
            it.dismiss()
            listener.invoke(it)
        }
    }

    fun questionDialog(
        context: Context,
        massage: String,
        yesListener: (Dialog) -> Unit,
        closeListener: (Dialog) -> Unit
    ): Dialog {
        return QuestionDialog(context, massage, yesListener, closeListener)
    }

    fun questionDialogWithTitle(
        context: Context,
        title: String,
        message: String,
        yesListener: (Dialog) -> Unit,
        closeListener: (Dialog) -> Unit
    ): Dialog {
        return QuestionDialogWithTitle(context, title, message, yesListener, closeListener)
    }

    fun successDialog(
        context: Context,
        title: String = "",
        massage: String,
        isCancelable: Boolean = false,
        listener: (Dialog) -> Unit
    ): Dialog {
        return SuccessDialog(context, title, massage, isCancelable, listener)
    }

    fun bookmarkDialog(context: Context, massage: String, yesListener: (Dialog) -> Unit, noListener: (Dialog) -> Unit): Dialog {
        return BookmarkDialog(context, massage, yesListener, noListener)
    }
}

fun Fragment.inputDialog(title: String, input: (String) -> Unit) {
    DialogUtil.input(requireContext(), title) { dialog, inputText ->
        dialog.dismiss()
        if (inputText.isNotEmpty()) {
            input(inputText)
        }
    }.show()
}