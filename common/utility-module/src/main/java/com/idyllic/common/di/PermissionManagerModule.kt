package com.idyllic.common.di


import com.idyllic.common.permission.PermissionManager
import com.idyllic.common.permission.PermissionManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.scopes.FragmentScoped


@Module
@InstallIn(FragmentComponent::class)
interface PermissionManagerModule {

    @Binds
    @FragmentScoped
    fun bindPermissionManager(app: PermissionManagerImpl): PermissionManager
}