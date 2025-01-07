package com.idyllic.yandexmaps.ui.screen.menu.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.idyllic.core.ktx.timber
import com.idyllic.core_api.model.LineDto
import com.idyllic.core_api.model.LinesBodyDto
import com.idyllic.core_api.model.LinesDto
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.usecase.GetLinesUseCase

class LinesDataSource(
    private val getLinesUseCase: GetLinesUseCase,
    private val linesBodyDto: LinesBodyDto
) : PagingSource<Int, LineDto>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LineDto> {
        val currentLoadingPageKey = params.key ?: 1
        val prevKey = if (currentLoadingPageKey == 1) null else currentLoadingPageKey - 1
        val invoke = getLinesUseCase.invoke(
            linesBodyDto.copy(page = currentLoadingPageKey)
        )
        var result: ResourceUI<LinesDto>? = null
        invoke.collect { result = it }

        return when (result) {
            is ResourceUI.Error -> LoadResult.Error((result as ResourceUI.Error).error)
            is ResourceUI.Success -> {
                val data = (result as ResourceUI.Success<LinesDto>).data
                val totalPages = data.meta?.pageCount
                val template = currentLoadingPageKey.plus(1)
                val nextKey = if (template < (totalPages ?: 0)) template else null
                val data1: List<LineDto> = data.lines ?: arrayListOf()
                timber("CAME LIST DATA: $data1")
                LoadResult.Page(data1, prevKey, nextKey)
            }

            else -> LoadResult.Error(NullPointerException())
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LineDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}