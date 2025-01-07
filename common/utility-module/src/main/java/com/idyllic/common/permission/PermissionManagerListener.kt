package com.idyllic.common.permission

interface PermissionManagerListener {
    fun onAllow()
    fun onDeny()
}