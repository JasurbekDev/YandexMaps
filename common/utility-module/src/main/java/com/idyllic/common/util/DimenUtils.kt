package com.idyllic.common.util

import android.content.Context
import android.content.res.Resources

val Int.dp: Int get() = (this / Resources.getSystem().displayMetrics.density).toInt()
val Int.px: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Context.dpToPx(dp: Int) = (dp * resources.displayMetrics.density).toInt()
fun Context.pxToDp(px: Int) = (px / resources.displayMetrics.density).toInt()