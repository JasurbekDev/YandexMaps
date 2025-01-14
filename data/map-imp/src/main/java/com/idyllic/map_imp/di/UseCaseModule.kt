package com.idyllic.map_imp.di

import com.idyllic.map_api.usecase.DeleteLocationDbUseCase
import com.idyllic.map_api.usecase.FullAddressUseCase
import com.idyllic.map_api.usecase.GetLocationsDbUseCase
import com.idyllic.map_api.usecase.InsertLocationsDbUseCase
import com.idyllic.map_api.usecase.SearchByTextUseCase
import com.idyllic.map_imp.usecase.DeleteLocationDbUseCaseImp
import com.idyllic.map_imp.usecase.FullAddressUseCaseImp
import com.idyllic.map_imp.usecase.GetLocationsDbUseCaseImp
import com.idyllic.map_imp.usecase.InsertLocationsDbUseCaseImp
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

    @Binds
    fun bindsGetLocationsDbUseCase(case: GetLocationsDbUseCaseImp): GetLocationsDbUseCase

    @Binds
    fun bindsInsertLocationsDbUseCase(case: InsertLocationsDbUseCaseImp): InsertLocationsDbUseCase

    @Binds
    fun bindsDeleteLocationDbUseCase(case: DeleteLocationDbUseCaseImp): DeleteLocationDbUseCase

}