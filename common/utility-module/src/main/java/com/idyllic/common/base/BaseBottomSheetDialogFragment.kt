package com.idyllic.common.base

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomSheetDialogFragment(@LayoutRes private val contentLayoutId: Int) :
    BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(contentLayoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.post {
            val parent = view.parent as? View
            parent?.let {
                val bottomSheetBehavior = BottomSheetBehavior.from(it)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                bottomSheetBehavior.skipCollapsed = true
            }
        }
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(this::class.simpleName) == null) show(
            manager, this::class.simpleName
        )
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL)

            attributes = attributes.apply {
                gravity = Gravity.BOTTOM
            }
        }
    }
}