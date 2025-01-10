package com.idyllic.common.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewGroupCompat
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.idyllic.ui_module.R
import timber.log.Timber

fun Fragment.setFromScreenMaterialSharedAxisZTransition() {
    reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
        duration = resources.getInteger(R.integer.motion_duration_medium).toLong()
    }
    exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
        duration = resources.getInteger(R.integer.motion_duration_medium).toLong()
    }
}

fun Fragment.setFromScreenMaterialSharedAxisZTransition(view: View) {
    try {
        ViewGroupCompat.setTransitionGroup(view as ViewGroup, true)
    } catch (e: ClassCastException) {
        Timber.e(e)
    } finally {
        setFromScreenMaterialSharedAxisZTransition()
    }
}

fun Fragment.setMaterialFadeThroughTransition() {
    exitTransition = MaterialFadeThrough().apply {
        duration = resources.getInteger(R.integer.motion_duration_medium).toLong()
    }

    enterTransition = MaterialFadeThrough().apply {
        duration = resources.getInteger(R.integer.motion_duration_medium).toLong()
    }
}

fun Fragment.setMaterialFadeThroughTransition(view: View) {
    try {
        ViewGroupCompat.setTransitionGroup(view as ViewGroup, true)
    } catch (e: ClassCastException) {
        Timber.e(e)
    } finally {
        setMaterialFadeThroughTransition()
    }
}