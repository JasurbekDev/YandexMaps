package com.idyllic.yandexmaps.ui.screen.map.ui.lines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.idyllic.common.base.BaseVM
import com.idyllic.core_api.model.LineDto
import com.idyllic.core_api.model.LinesBodyDto
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.usecase.GetLinesUseCase
import com.idyllic.yandexmaps.ui.screen.map.source.LinesDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class LinesScreenVM @Inject constructor(
    private val getLinesUseCase: GetLinesUseCase
) : BaseVM() {

    var linePager: Flow<PagingData<LineDto>>? = null

    private val _linesLiveData = MutableLiveData<List<LineDto>?>()
    val linesLiveData: LiveData<List<LineDto>?> = _linesLiveData

    init {
        getLines()
    }

    private fun getLines() {
        linePager = Pager(PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = true)) {
            val linesBodyDto = LinesBodyDto(page = 1)
            LinesDataSource(
                getLinesUseCase,
                linesBodyDto
            )
        }.flow.cachedIn(viewModelScope)
    }

    private fun getLines2() {
        showGlobalLoading()
        launchVM {
            val linesBodyDto = LinesBodyDto(page = 1)
            getLinesUseCase.invoke(linesBodyDto).collect {
                when (it) {
                    is ResourceUI.Error -> {
                        globalError(it.error)
                    }
                    is ResourceUI.Success -> {
                        val data = it.data
                        _linesLiveData.value = data.lines
                    }
                }
                hideGlobalLoading()
            }
        }
    }
}