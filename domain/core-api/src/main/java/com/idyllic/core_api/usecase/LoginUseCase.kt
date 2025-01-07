package com.idyllic.core_api.usecase

import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.model.LoginBodyDto
import com.idyllic.core_api.model.UserDto
import kotlinx.coroutines.flow.Flow

interface LoginUseCase {

    suspend fun invoke(dto: LoginBodyDto): Flow<ResourceUI<UserDto>>

}