package com.idyllic.yandexmaps.ui.screen.map.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.idyllic.common.util.getRandomNumberForString
import com.idyllic.common.vm.SingleLiveEvent
import com.idyllic.core.ktx.timber
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.map_api.model.LocationDto
import com.idyllic.map_api.usecase.FullAddressUseCase
import com.idyllic.map_api.usecase.InsertLocationsDbUseCase
import com.idyllic.yandexmaps.base.BaseMainVM
import com.idyllic.yandexmaps.models.GeoObjectLocation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapScreenVM @Inject constructor(
    private val fullAddressUseCase: FullAddressUseCase,
    private val insertLocationsDbUseCase: InsertLocationsDbUseCase,
    ssh: SavedStateHandle
) : BaseMainVM() {

    private val _locationDto: LocationDto? = ssh.get<LocationDto>("location_dto")

    private var _zoom: Float? = null
    private var _azimuth: Float? = null
    private var _tilt: Float? = null
    private var _mapCenter: Point? = null
    private var _placeMark: Pair<Point, GeoObjectLocation>? = null
    private var _selectedGeoObject: Pair<GeoObjectSelectionMetadata, GeoObjectLocation>? = null
    private var job: Job? = null

    private val _selectGeoObjectLiveData =
        SingleLiveEvent<Pair<GeoObjectSelectionMetadata, GeoObjectLocation>>()
    val selectGeoObjectLiveData: LiveData<Pair<GeoObjectSelectionMetadata, GeoObjectLocation>> =
        _selectGeoObjectLiveData

    private val _createPinLiveData = SingleLiveEvent<Pair<Point, GeoObjectLocation>>()
    val createPinLiveData: LiveData<Pair<Point, GeoObjectLocation>> = _createPinLiveData

    private val _centerPinDropLiveData = SingleLiveEvent<Pair<Point, GeoObjectLocation>>()
    val centerPinDropLiveData: LiveData<Pair<Point, GeoObjectLocation>> = _centerPinDropLiveData

    private val _bookmarkLiveData = SingleLiveEvent<LocationDto>()
    val bookmarkLiveData: LiveData<LocationDto> = _bookmarkLiveData

    val zoom: Float?
        get() = _zoom

    val azimuth: Float?
        get() = _azimuth

    val tilt: Float?
        get() = _tilt

    val mapCenter: Point?
        get() = _mapCenter

    val placeMark: Pair<Point, GeoObjectLocation>?
        get() = _placeMark

    val selectedGeoObject: Pair<GeoObjectSelectionMetadata, GeoObjectLocation>?
        get() = _selectedGeoObject

    init {
        _locationDto?.let {
            _bookmarkLiveData.value = it
            _placeMark = null
            _selectedGeoObject = null
        }
    }

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

    fun setPlaceMark(point: Point, geoObjectLocation: GeoObjectLocation) {
        this._placeMark = Pair(point, geoObjectLocation)
    }

    fun clearPlaceMark() {
        _placeMark = null
    }

    fun setSelectedGeoObject(
        selectedGeoObjectMetadata: GeoObjectSelectionMetadata?,
        geoObjectLocation: GeoObjectLocation?
    ) {
        if (selectedGeoObjectMetadata == null || geoObjectLocation == null) {
            this._selectedGeoObject = null
            return
        }
        this._selectedGeoObject = Pair(selectedGeoObjectMetadata, geoObjectLocation)
    }

    fun selectGeoObject(
        metadata: GeoObjectSelectionMetadata,
        point: Point,
        name: String,
        geoObjectLocation: GeoObjectLocation? = null
    ) {
        _placeMark = null
        geoObjectLocation?.let {
            setSelectedGeoObject(metadata, geoObjectLocation)
            _selectGeoObjectLiveData.value =
                Pair(metadata, geoObjectLocation)
            return
        }
        getFullAddress(point, SelectionType.GEO_OBJECT, metadata, name)
    }

    fun createPin(point: Point) {
        clearPlaceMark()
        setSelectedGeoObject(null, null)
        getFullAddress(point, SelectionType.FIXED_PIN)
    }

    fun onSearchResultSelect() {
        _selectedGeoObject = null
        job?.cancel()
    }

    private fun dropCenterPin() {
        _mapCenter?.let { center ->
            getFullAddress(center, SelectionType.CENTER_PIN)
        }
    }

    private fun getFullAddress(
        point: Point,
        selectionType: SelectionType,
        metadata: GeoObjectSelectionMetadata? = null,
        name: String? = null
    ) {
        launchVM {
            val lat = point.latitude
            val lon = point.longitude

            fullAddressUseCase.invoke(lat, lon).collect {
                when (it) {
                    is ResourceUI.Error -> {
                        globalError(it.error)
                    }

                    is ResourceUI.Success -> {
                        val data = it.data

                        when (selectionType) {
                            SelectionType.GEO_OBJECT -> {
                                metadata?.let {
                                    val geoObjectLocation = GeoObjectLocation(
                                        name = name,
                                        address = data,
                                        rating = getRandomNumberForString(name ?: "", 0..5),
                                        reviews = getRandomNumberForString(data, 0..999),
                                        lat = point.latitude,
                                        lon = point.longitude
                                    )
                                    setSelectedGeoObject(metadata, geoObjectLocation)
                                    _selectGeoObjectLiveData.value =
                                        Pair(metadata, geoObjectLocation)
                                }
                            }

                            SelectionType.FIXED_PIN -> {
                                val geoObjectLocation = GeoObjectLocation(
                                    name = name,
                                    address = data,
                                    rating = null,
                                    reviews = null,
                                    lat = point.latitude,
                                    lon = point.longitude
                                )
                                setPlaceMark(point, geoObjectLocation)
                                _createPinLiveData.value = Pair(point, geoObjectLocation)
                            }

                            SelectionType.CENTER_PIN -> {
                                val geoObjectLocation = GeoObjectLocation(
                                    name = name,
                                    address = data,
                                    rating = null,
                                    reviews = null,
                                    lat = point.latitude,
                                    lon = point.longitude
                                )
                                _centerPinDropLiveData.value = Pair(point, geoObjectLocation)
                            }
                        }
                        timber("ADDRESSSSS: $data")
                    }
                }
            }
        }
    }

    fun onCameraPositionChanged() {
        job?.cancel()
    }

    fun onCameraPositionChangedFinish() {
        job = viewModelScope.launch {
            delay(400)
            if (isCenterPinActive()) {
                dropCenterPin()
            }
        }
    }

    fun onDestroyView() {
        job?.cancel()
    }

    fun onSearchDialogOpen() {
        job?.cancel()
    }

    fun isCenterPinActive(): Boolean = _placeMark == null && _selectedGeoObject == null

    fun saveToBookmarks(geoObjectLocation: GeoObjectLocation) {
        launchVM {
            val locationDto = LocationDto(
                name = geoObjectLocation.name,
                street = geoObjectLocation.address,
                lat = geoObjectLocation.lat,
                lon = geoObjectLocation.lon,
                rating = geoObjectLocation.rating,
                reviews = geoObjectLocation.reviews
            )
            insertLocationsDbUseCase.invoke(listOf(locationDto))
        }
    }

    enum class SelectionType {
        GEO_OBJECT,
        FIXED_PIN,
        CENTER_PIN
    }

}