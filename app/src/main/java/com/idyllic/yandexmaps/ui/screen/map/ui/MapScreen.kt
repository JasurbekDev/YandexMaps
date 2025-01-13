package com.idyllic.yandexmaps.ui.screen.map.ui

import android.graphics.PointF
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.core.ktx.backPressedScreen
import com.idyllic.core.ktx.gone
import com.idyllic.core.ktx.timber
import com.idyllic.core.ktx.visible
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.base.BaseMainFragment
import com.idyllic.yandexmaps.databinding.ScreenMapBinding
import com.idyllic.yandexmaps.models.GeoObjectLocation
import com.idyllic.yandexmaps.ui.dialog.LocationDialogInteractable
import com.idyllic.yandexmaps.ui.dialog.SearchLocationDialog
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.layers.GeoObjectTapListener
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.GeoObjectSelectionMetadata
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.Map.CameraCallback
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PlacemarkMapObject
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session.SearchListener
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapScreen : BaseMainFragment(R.layout.screen_map), View.OnClickListener, CameraListener {
    override val viewModel: MapScreenVM by viewModels()
    private val binding by viewBinding(ScreenMapBinding::bind)
    private var mainNavigation: NavController? = null
    private var map: Map? = null
    private var placeMark: PlacemarkMapObject? = null
    private var locationDialog: LocationDialogInteractable? = null

    private val placeMarkTapListener = MapObjectTapListener { mapObject, point ->
        try {
            val geoObjectLocation = mapObject.userData as? GeoObjectLocation
//            toast("Tapped the point (${point.longitude}, ${point.latitude})")

            map?.let {
                moveCamera(it, point)
            }
            showLocationDialog(geoObjectLocation)
        } catch (e: ClassCastException) {
            Timber.e(e)
        }
        true
    }

    private val inputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {

        }

        override fun onMapLongTap(map: Map, point: Point) {
            timber("${point.latitude} ${point.longitude}")
//            clearAllPins(map)
            map.mapObjects.clear()
            deselectGeoObject()
            disableCenterPin()
            viewModel.createPin(point)
            moveCamera(map, point)
        }

    }

    private fun moveCamera(map: Map, point: Point) {
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

    private val cameraCallback = CameraCallback { isFinished ->
        if (isFinished) {
        }
    }

    private val geoObjectTapListener = GeoObjectTapListener { event ->
        val metadataContainer = event.geoObject.metadataContainer
        val selectionMetadata = metadataContainer.getItem(GeoObjectSelectionMetadata::class.java)

        val point = event.geoObject.geometry[0].point

        point?.let {
            map?.mapObjects?.clear()
            placeMark = null
            val name = event.geoObject.name ?: ""
            viewModel.selectGeoObject(selectionMetadata, point, name)
            map?.let {
                moveCamera(it, point)
            }
            return@GeoObjectTapListener true
        }
        return@GeoObjectTapListener false
    }

    private fun getAddressOf(point: Point): String {
        val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        var fullAddress = ""
        searchManager.submit(
            point,
            0,
            SearchOptions(),
            object : SearchListener {
                override fun onSearchResponse(response: Response) {
                    val address = response.collection.children.firstOrNull()?.obj?.name
                    val desc = response.collection.children.firstOrNull()?.obj?.descriptionText ?: ""

                    val descParts = desc.split(",").map { it.trim() }.reversed()
                    val descWithoutLast = if (descParts.size > 1) descParts.drop(1).joinToString(", ") else ""

                    fullAddress = if (descWithoutLast.isNotEmpty()) "$descWithoutLast, $address" else address ?: ""
                }

                override fun onSearchError(error: Error) {
                    Timber.e("ReverseGeocoding", "Error: $error")
                }

            }
        )
        return fullAddress
    }

    private fun selectGeoObject(selectionMetadata: GeoObjectSelectionMetadata, geoObjectLocation: GeoObjectLocation) {
        disableCenterPin()
        map?.selectGeoObject(selectionMetadata)
        showLocationDialog(geoObjectLocation)
    }

    private fun showLocationDialog(geoObjectLocation: GeoObjectLocation? = null) {


        val existingDialog = childFragmentManager.findFragmentByTag("LocationDialogInteractable") as? LocationDialogInteractable
        val searchLocationDialog = childFragmentManager.findFragmentByTag("SearchLocationDialog") as? SearchLocationDialog

        if (searchLocationDialog == null || searchLocationDialog.isDetached) {
            if (existingDialog != null) {
                locationDialog = existingDialog
                locationDialog?.setGeoObjectLocation(geoObjectLocation)
            } else {
                locationDialog = LocationDialogInteractable.newInstance(geoObjectLocation)
                locationDialog?.show(childFragmentManager)
            }
        }






//        if (viewModel.isOpenDialog) {
//            if (locationDialog == null) {
//                locationDialog = LocationDialog.newInstance(geoObjectLocation)
//                locationDialog?.show(childFragmentManager)
//            } else {
//                locationDialog?.setGeoObjectLocation(geoObjectLocation)
//                locationDialog?.show(childFragmentManager)
//            }
//        } else {
//            locationDialog?.setGeoObjectLocation(geoObjectLocation)
//            viewModel.setOpenDialogTrue()
//        }
    }

    private fun deselectGeoObject() {
        enableCenterPin()
        map?.deselectGeoObject()
        viewModel.setSelectedGeoObject(null, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        initMap()

        viewModel.apply {
            locationDialogLiveData.observe(viewLifecycleOwner, locationDialogObserver)
            selectGeoObjectLiveData.observe(viewLifecycleOwner, selectGeoObjectObserver)
            createPinLiveData.observe(viewLifecycleOwner, createPinObserver)
            centerPinDropLiveData.observe(viewLifecycleOwner, centerPinDropObserver)
        }

        viewModel.placeMarkGeometry?.let {
            createPin(it.first, it.second)
        }

        viewModel.selectedGeoObject?.let { obj ->
            selectGeoObject(obj.first, obj.second)
        }

        backPressedScreen(true) {
            if (placeMark == null && viewModel.selectedGeoObject == null) {
                requireActivity().moveTaskToBack(true)
            } else {
                clearAllPins(map)
            }
        }

        binding.cardSearch.setOnClickListener(this)
    }

    private val locationDialogObserver = Observer<Point?> {
        it?.let { point ->
            val geoObjectLocation = GeoObjectLocation(
                address = getAddressOf(point)
            )
            showLocationDialog(geoObjectLocation)
        }
    }

    private val selectGeoObjectObserver = Observer<Pair<GeoObjectSelectionMetadata, GeoObjectLocation>> {
        selectGeoObject(it.first, it.second)
    }

    private val createPinObserver = Observer<Pair<Point, GeoObjectLocation>> {
        createPin(it.first, it.second)
    }

    private val centerPinDropObserver = Observer<Pair<Point, GeoObjectLocation>> {
        showLocationDialog(it.second)
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

    private fun createPin(point: Point, geoObjectLocation: GeoObjectLocation) {
//        clearAllPins(map)
        disableCenterPin()

        val imageProvider =
            ImageProvider.fromResource(context, com.idyllic.ui_module.R.drawable.ic_pin)
        placeMark = map?.mapObjects?.addPlacemark()?.apply {
            geometry = point
            setIcon(imageProvider)
        }
        placeMark?.userData = geoObjectLocation
        placeMark?.setIconStyle(
            IconStyle().apply {
                anchor = PointF(0.5f, 0.95f)
            }
        )
        placeMark?.addTapListener(placeMarkTapListener)
        showLocationDialog(geoObjectLocation)
    }

    private fun clearAllPins(map: Map?) {
        map?.mapObjects?.clear()
        viewModel.clearPlaceMark()
        placeMark = null
        deselectGeoObject()
        enableCenterPin()
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
        viewModel.onCameraPositionChanged()
        viewModel.setZoom(cameraPosition.zoom)
        viewModel.setAzimuth(cameraPosition.azimuth)
        viewModel.setTilt(cameraPosition.tilt)
        viewModel.setMapCenter(cameraPosition.target)

        timber("MAPCENTERRR2: ${viewModel.mapCenter?.latitude} ${viewModel.mapCenter?.longitude}")
        if (finished) {
            if (viewModel.isCenterPinActive()) {
                viewModel.onCameraPositionChangedFinish()
            }
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

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onDestroyView()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.card_search -> {
                viewModel.onSearchDialogOpen()
                SearchLocationDialog.newInstance().show(childFragmentManager)
            }
        }
    }
}