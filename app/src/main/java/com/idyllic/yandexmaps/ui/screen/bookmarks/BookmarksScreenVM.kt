package com.idyllic.yandexmaps.ui.screen.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.idyllic.map_api.model.LocationDto
import com.idyllic.map_api.usecase.DeleteLocationDbUseCase
import com.idyllic.map_api.usecase.GetLocationsDbUseCase
import com.idyllic.yandexmaps.base.BaseMainVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarksScreenVM @Inject constructor(
    private val getLocationsDbUseCase: GetLocationsDbUseCase,
    private val deleteLocationDbUseCase: DeleteLocationDbUseCase,
) : BaseMainVM() {

    private val _locationsLiveData = MutableLiveData<List<LocationDto>>()
    val locationsLiveData: LiveData<List<LocationDto>> = _locationsLiveData

    init {
        getLocations()
    }

    fun getLocations() {
        launchVM {
            val data = getLocationsDbUseCase.invoke()
            _locationsLiveData.value = data.reversed()
        }
    }

    fun removeBookmark(position: Int) {
        launchVM {
            val location = _locationsLiveData.value?.get(position)
            location?.lat?.let { lat ->
                location.lon?.let { lon ->
                    deleteLocationDbUseCase.invoke(lat, lon)
                }
            }
        }
    }
}