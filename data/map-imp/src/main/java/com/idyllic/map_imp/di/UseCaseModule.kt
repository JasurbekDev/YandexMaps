package com.idyllic.map_imp.di

import com.idyllic.map_api.usecase.FullAddressUseCase
import com.idyllic.map_api.usecase.SearchByTextUseCase
import com.idyllic.map_imp.usecase.FullAddressUseCaseImp
import com.idyllic.map_imp.usecase.SearchByTextUseCaseImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {

    @Binds
    fun bindsFullAddressUseCase(case: FullAddressUseCaseImp): FullAddressUseCase

    @Binds
    fun bindsSearchByTextUseCase(case: SearchByTextUseCaseImp): SearchByTextUseCase

}