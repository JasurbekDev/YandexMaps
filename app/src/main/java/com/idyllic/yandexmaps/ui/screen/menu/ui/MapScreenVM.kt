package com.idyllic.yandexmaps.ui.screen.menu.ui

import com.idyllic.core_api.usecase.LoginUseCase
import com.idyllic.yandexmaps.base.BaseMainVM
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapScreenVM @Inject constructor(
    private val loginUseCase: LoginUseCase
) : BaseMainVM() {

    private var _zoom: Float? = null
    private var _azimuth: Float? = null
    private var _tilt: Float? = null
    private var _mapCenter: Point? = null
    private var _placeMarkGeometry: Point? = null
    private var _selectedGeoObject: Pair<GeoObjectSelectionMetadata, Point>? = null

    val zoom: Float?
        get() = _zoom

    val azimuth: Float?
        get() = _azimuth

    val tilt: Float?
        get() = _tilt

    val mapCenter: Point?
        get() = _mapCenter

    val placeMarkGeometry: Point?
        get() = _placeMarkGeometry

    val selectedGeoObject: Pair<GeoObjectSelectionMetadata, Point>?
        get() = _selectedGeoObject

    fun setZoom(zoom: Float?) {
        this._zoom = zoom
    }

    fun setAzimuth(azimuth: Float?) {
        this._azimuth = azimuth
    }

    fun setTilt(tilt: Float?) {
        this._tilt = tilt
    }

    fun setMapCenter(mapCenter: Point?) {
        this._mapCenter = mapCenter
    }

    fun setPlaceMarkGeometry(placeMarkGeometry: Point?) {
        this._placeMarkGeometry = placeMarkGeometry
    }

    fun clearPlaceMark() {
        _placeMarkGeometry = null
    }

    fun setSelectedGeoObject(selectedGeoObjectMetadata: GeoObjectSelectionMetadata?, point: Point?) {
        if (selectedGeoObjectMetadata == null || point == null) {
            this._selectedGeoObject = null
            return
        }
        this._selectedGeoObject = Pair(selectedGeoObjectMetadata, point)
    }

}