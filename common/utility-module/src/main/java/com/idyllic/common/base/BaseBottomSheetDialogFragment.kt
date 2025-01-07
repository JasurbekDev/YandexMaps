package com.idyllic.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentManager
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

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(this::class.simpleName) == null) show(
            manager, this::class.simpleName
        )
    }
}