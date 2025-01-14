package com.idyllic.yandexmaps.ui.dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.idyllic.common.base.BaseVM
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.map_api.model.LocationDto
import com.idyllic.map_api.usecase.SearchByTextUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class SearchLocationDialogVM @Inject constructor(
    private val searchByTextUseCase: SearchByTextUseCase
) : BaseVM() {

    private val _searchResultLiveData = MutableLiveData<List<LocationDto>>()
    val searchResultLiveData: LiveData<List<LocationDto>> = _searchResultLiveData

    private var job: Job? = null

    fun searchLocation(query: String) {
        job?.cancel()
        job = launchVM {
            delay(500)
            showGlobalLoading()
            searchByTextUseCase.invoke(query, 41.312046, 69.279947)
                .collect {
                    when (it) {
                        is ResourceUI.Error -> {
                            globalError(it.error)
                        }

                        is ResourceUI.Success -> {
                            val data = it.data
                            _searchResultLiveData.value = data
                        }
                    }
                }
            hideGlobalLoading()
        }
    }

}