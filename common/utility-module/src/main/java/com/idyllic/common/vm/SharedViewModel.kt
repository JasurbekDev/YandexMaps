package com.idyllic.common.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.idyllic.map_api.model.LocationDto
import javax.inject.Inject

class SharedViewModel @Inject constructor() : ViewModel() {

    private val _bookmarkLiveData = SingleLiveEvent<LocationDto>()
    val bookmarkLiveData: LiveData<LocationDto> = _bookmarkLiveData

    fun showBookmarkOnMap(locationDto: LocationDto) {
        _bookmarkLiveData.value = locationDto
    }

}