package com.idyllic.yandexmaps.ui.screen.menu.ui

import android.graphics.PointF
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.core.ktx.backPressedScreen
import com.idyllic.core.ktx.gone
import com.idyllic.core.ktx.timber
import com.idyllic.core.ktx.toast
import com.idyllic.core.ktx.visible
import com.idyllic.core_api.model.LineDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.base.BaseMainFragment
import com.idyllic.yandexmaps.databinding.ScreenMapBinding
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapEvent
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.Map.CameraCallback
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import kotlin.getValue

@AndroidEntryPoint
class MapScreen : BaseMainFragment(R.layout.screen_map), View.OnClickListener, CameraListener {
    override val viewModel: MapScreenVM by viewModels()
    private val binding by viewBinding(ScreenMapBinding::bind)
    private var mainNavigation: NavController? = null
    private var map: Map? = null
    private var placeMark: PlacemarkMapObject? = null

    private val placeMarkTapListener = MapObjectTapListener { mapObject, point ->
        try {
            val userCarDto = mapObject.userData as LineDto.UserCarDto
//            toast("Tapped the point (${point.longitude}, ${point.latitude})")
            toast("User id ${userCarDto.id}")
        } catch (e: ClassCastException) {
            Timber.e(e)
        }
        true
    }

    val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {

        }

        override fun onMapLongTap(map: Map, point: Point) {
            timber("${point.latitude} ${point.longitude}")
            disableCenterPin()
            clearAllPins(map)
            createPin(map, point, placeMarkTapListener)

            map.move(
                CameraPosition(
                    point,
                    map.cameraPosition.zoom,
                    viewModel.azimuth ?: 16.0f,
                    viewModel.tilt ?: 0.0f
                ),
                Animation(Animation.Type.SMOOTH, 0.5f),
                cameraCallback
            )
        }

    }

    val cameraCallback = object : CameraCallback {
        override fun onMoveFinished(isFinished: Boolean) {
            if (isFinished) {

            }
        }
    }

    val geoObjectTapListener = object : GeoObjectTapListener {
        override fun onObjectTap(event: GeoObjectTapEvent): Boolean {
            timber("onObjectTap: ${event.geoObject.name}")
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        initMap()

        viewModel.placeMarkGeometry?.let {
            createPin(map, it, placeMarkTapListener)
            disableCenterPin()
        }

        backPressedScreen(true) {
            if (placeMark == null) {
                requireActivity().moveTaskToBack(true)
            } else {
                clearAllPins(map)
                enableCenterPin()
            }
        }
    }

    private fun initMap() {
        map = binding.mapView.mapWindow.map
        map?.addCameraListener(this)
        map?.addInputListener(inputListener)
        map?.addTapListener(geoObjectTapListener)
        map?.move(
            CameraPosition(
                viewModel.mapCenter ?: Point(41.312046, 69.279947),
                viewModel.zoom ?: 16.0f,
                viewModel.azimuth ?: 16.0f,
                viewModel.tilt ?: 0.0f
            )
        )
    }

    private fun disableCenterPin() {
        binding.imagePin.gone()
    }

    private fun enableCenterPin() {
        binding.imagePin.visible()
    }

    private fun createPin(map: Map?, point: Point, listener: MapObjectTapListener) {
        val imageProvider =
            ImageProvider.fromResource(context, com.idyllic.ui_module.R.drawable.ic_pin)
        placeMark = map?.mapObjects?.addPlacemark()?.apply {
            geometry = point
            setIcon(imageProvider)
        }
        placeMark?.isDraggable = true
        placeMark?.userData = LineDto.UserCarDto(123)
        placeMark?.setIconStyle(
            IconStyle().apply {
                anchor = PointF(0.5f, 0.95f)
            }
        )
        placeMark?.addTapListener(listener)
        placeMark?.let { pm ->
            viewModel.setPlaceMarkGeometry(pm.geometry)
        }
    }

    private fun clearAllPins(map: Map?) {
        map?.mapObjects?.clear()
        placeMark = null
    }

    private fun addPlaceMarkAtPosition(position: Point) {
        val imageProvider =
            ImageProvider.fromResource(context, com.idyllic.ui_module.R.drawable.ic_pin)
        val mapObjects = map?.mapObjects
        placeMark = mapObjects?.addPlacemark()?.apply {
            geometry = position
            setIcon(imageProvider)
        }
    }

    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finished: Boolean
    ) {
        viewModel.setZoom(cameraPosition.zoom)
        viewModel.setAzimuth(cameraPosition.azimuth)
        viewModel.setTilt(cameraPosition.tilt)
        viewModel.setMapCenter(cameraPosition.target)

        timber("MAPCENTERRR2: ${viewModel.mapCenter?.latitude} ${viewModel.mapCenter?.longitude}")
        if (finished && placeMark == null) {
        }
    }

    private fun updatePinPosition(position: Point) {
        placeMark?.geometry = position
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }
}