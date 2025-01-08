package com.idyllic.yandexmaps.ui.screen.menu.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.core.ktx.toast
import com.idyllic.core_api.model.LineDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.base.BaseMainFragment
import com.idyllic.yandexmaps.databinding.ScreenMapBinding
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapScreen : BaseMainFragment(R.layout.screen_map), View.OnClickListener {
    override val viewModel: MenuScreenVM by viewModels()
    private val binding by viewBinding(ScreenMapBinding::bind)
    private var mainNavigation: NavController? = null
    private var map: Map? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        map = binding.mapView.mapWindow.map

        map?.move(
            CameraPosition(
                Point(41.312046, 69.279947),
                /* zoom = */ 16.0f,
                /* azimuth = */ 16.0f,
                /* tilt = */ 0.0f
            )
        )

//        val cameraListener = object : CameraListener {
//            override fun onCameraPositionChanged(
//                p0: Map,
//                p1: CameraPosition,
//                p2: CameraUpdateReason,
//                p3: Boolean
//            ) {
//                timber("CHANGED!")
//            }
//        }
//
//        map?.addCameraListener(cameraListener)

        val imageProvider =
            ImageProvider.fromResource(context, com.idyllic.ui_module.R.drawable.ic_pin)
        val placeMark = map?.mapObjects?.addPlacemark()?.apply {
            geometry = Point(41.312046, 69.279947)
            setIcon(imageProvider)
        }

        placeMark?.isDraggable = true
        placeMark?.userData = LineDto.UserCarDto(123)


        placeMark?.addTapListener(placeMarkTapListener)
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