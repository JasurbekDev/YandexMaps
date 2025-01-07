package com.idyllic.common.util

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator

/**
 * Copyright 2019, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 08/13/2019
 */

fun createDefaultNavOptions(destination: Int) =
    NavOptions.Builder().setLaunchSingleTop(false).setPopUpTo(destination, false)
        .setEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_right_to_left)
        .setExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_right_to_left)
        .setPopEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_left_to_right)
        .setPopExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_left_to_right).build()

fun NavController.navigateUriWithDefaultOptions(
    uri: Uri, extras: FragmentNavigator.Extras? = null
) {
    this.navigate(uri, createDefaultNavOptions(this.currentDestination?.id ?: -1), extras)
}

fun NavController.safeNavigate(direction: NavDirections) {
    currentDestination?.getAction(direction.actionId)?.run { navigate(direction) }
}

/**
 * Use this function only when the destination/navigation graph you want to navigate to
 * is in main_nav. Otherwise, use the built-in NavController.navigate() function.
 */
fun NavController.safeNavigateFromMainNav(
    deepLinkRequest: NavDeepLinkRequest,
    options: NavOptions
) {
    val mainNavGraphResourceId = context.resources.getIdentifier(
        "main_nav", "id", context.packageName
    )
    val currentGraphResourceName = context.resources.getResourceName(graph.id)
    val mainNavResourceName = context.resources.getResourceName(mainNavGraphResourceId)

    if (currentGraphResourceName.equals(mainNavResourceName)) {
        navigate(deepLinkRequest, options)
    }
}