package com.idyllic.map_imp.di

import com.idyllic.map_api.usecase.FullAddressUseCase
import com.idyllic.map_imp.usecase.FullAddressUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    @Binds
    fun bindsFullAddressUseCase(case: FullAddressUseCaseImp): FullAddressUseCase
}