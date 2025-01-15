package com.idyllic.yandexmaps.ui.screen.bookmarks

import androidx.lifecycle.viewModelScope
import com.idyllic.map_api.model.LocationDto
import com.idyllic.map_api.usecase.DeleteLocationDbUseCase
import com.idyllic.map_api.usecase.GetLocationsDbUseCase
import com.idyllic.yandexmaps.base.BaseMainVM
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksScreenVM @Inject constructor(
    private val getLocationsDbUseCase: GetLocationsDbUseCase,
    private val deleteLocationDbUseCase: DeleteLocationDbUseCase,
) : BaseMainVM() {

    private val _bookmarks = MutableStateFlow<List<LocationDto>>(emptyList())
    val bookmarks: StateFlow<List<LocationDto>> = _bookmarks.asStateFlow()

    init {
        fetchLocations()
    }

    private fun fetchLocations() {
        viewModelScope.launch {
            getLocationsDbUseCase.invoke()
                .collect { locationDtos ->
                    _bookmarks.value = locationDtos
                }
        }
    }

    fun removeBookmark(position: Int) {
        launchVM {
            val location = _bookmarks.value[position]
            location.lat?.let { lat ->
                location.lon?.let { lon ->
                    deleteLocationDbUseCase.invoke(lat, lon)
                }
            }
        }
    }
}