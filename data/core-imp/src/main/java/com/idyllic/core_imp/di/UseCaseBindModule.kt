package com.idyllic.core_imp.di

import com.idyllic.core_api.usecase.GetLinesUseCase
import com.idyllic.core_api.usecase.GetProductUseCase
import com.idyllic.core_api.usecase.LoginUseCase
import com.idyllic.core_imp.usecase.GetLinesUseCaseImp
import com.idyllic.core_imp.usecase.GetProductUseCaseImp
import com.idyllic.core_imp.usecase.LoginUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseBindModule {

    @Binds
    fun bindsGetProductUseCase(case: GetProductUseCaseImp): GetProductUseCase

    @Binds
    fun bindsLoginUseCase(case: LoginUseCaseImp): LoginUseCase

    @Binds
    fun bindsGetLinesUseCase(case: GetLinesUseCaseImp): GetLinesUseCase

}