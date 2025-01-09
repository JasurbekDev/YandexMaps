package com.idyllic.yandexmaps.ui.screen.menu.ui

import androidx.lifecycle.LiveData
import com.idyllic.common.vm.SingleLiveEvent
import com.idyllic.core.ktx.timber
import com.idyllic.core_api.model.LoginBodyDto
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.model.UserDto
import com.idyllic.core_api.usecase.LoginUseCase
import com.idyllic.yandexmaps.base.BaseMainVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapScreenVM @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseMainVM() {

    private val _loginLiveData = SingleLiveEvent<UserDto>()
    val loginLiveData: LiveData<UserDto> = _loginLiveData

}