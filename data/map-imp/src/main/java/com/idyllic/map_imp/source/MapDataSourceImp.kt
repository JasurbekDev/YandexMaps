package com.idyllic.map_imp.source

import com.idyllic.core_api.model.ResourceUI
import com.idyllic.map_api.model.LocationDto
import com.idyllic.map_api.source.MapDataSource
import com.idyllic.map_imp.model.Location
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resumeWithException
import kotlin.math.*

class MapDataSourceImp @Inject constructor() : MapDataSource {

    override suspend fun getFullAddress(
        lat: Double,
        lon: Double
    ): Flow<ResourceUI<String>> = flow {
        try {
            val address = getAddressFromCoordinates(lat, lon)
            emit(ResourceUI.Success(address))
        } catch (e: Exception) {
            emit(ResourceUI.Error(Exception(e.message)))
        }
    }

    override suspend fun searchByText(
        query: String,
        lat: Double,
        lon: Double
    ): Flow<ResourceUI<List<LocationDto>>> = flow {
        try {
            val results = searchLocations(query, lat, lon)
            emit(ResourceUI.Success(results.map { it.toDto() }))
        } catch (e: Exception) {
            emit(ResourceUI.Error(Exception(e.message)))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getAddressFromCoordinates(lat: Double, lon: Double): String =
        suspendCancellableCoroutine { continuation ->
            val searchManager =
                SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

            searchManager.submit(
                Point(lat, lon),
                0,
                SearchOptions(),
                object : Session.SearchListener {
                    override fun onSearchResponse(response: Response) {
                        val address = response.collection.children.firstOrNull()?.obj?.name
                        val desc =
                            response.collection.children.firstOrNull()?.obj?.descriptionText ?: ""

                        val descParts = desc.split(",").map { it.trim() }.reversed()
                        val descWithoutLast =
                            if (descParts.size > 1) descParts.drop(1).joinToString(", ") else ""

                        val fullAddress =
                            if (descWithoutLast.isNotEmpty()) "$descWithoutLast, $address" else address
                                ?: ""
                        continuation.resume(fullAddress) {}
                    }

                    override fun onSearchError(error: Error) {
                        Timber.e("ReverseGeocoding Error: $error")
                        continuation.resumeWithException(Exception(error.toString()))
                    }
                }
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun searchLocations(query: String, lat: Double, lon: Double): List<Location> =
        suspendCancellableCoroutine { continuation ->
            val searchManager =
                SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
            val searchPoint = Point(lat, lon)
            val geometry = Geometry.fromPoint(searchPoint)

            searchManager.submit(
                query,
                geometry,
                SearchOptions(),
                object : Session.SearchListener {
                    override fun onSearchResponse(response: Response) {
                        val searchResults = response.collection.children

                        val locations = arrayListOf<Location>()

                        for (result in searchResults) {
                            result?.obj?.metadataContainer


                            val name = result?.obj?.name
                            val point = result?.obj?.geometry?.firstOrNull()?.point
                            var distance: Int? = null
                            point?.let {
                                distance = calculateDistance(Point(lat, lon), point).toInt()
                            }

                            val address = response.collection.children.firstOrNull()?.obj?.name
                            val desc =
                                response.collection.children.firstOrNull()?.obj?.descriptionText
                                    ?: ""

                            val descParts = desc.split(",").map { it.trim() }.reversed()
                            val descWithoutLast =
                                if (descParts.size > 1) descParts.drop(1).joinToString(", ") else ""

                            val fullAddress =
                                if (descWithoutLast.isNotEmpty()) "$descWithoutLast, $address" else address
                                    ?: ""

                            locations.add(
                                Location(
                                    name = name,
                                    street = fullAddress,
                                    distance = distance,
                                    lat = point?.latitude,
                                    lon = point?.longitude
                                )
                            )
                        }

                        continuation.resume(locations) {}
                    }

                    override fun onSearchError(error: Error) {
                        Timber.e("ReverseGeocoding Error: $error")
                        continuation.resumeWithException(Exception(error.toString()))
                    }
                }
            )
        }

    fun calculateDistance(point1: Point, point2: Point): Double {
        val earthRadius = 6371000.0 // Radius of Earth in meters

        val lat1 = Math.toRadians(point1.latitude)
        val lon1 = Math.toRadians(point1.longitude)
        val lat2 = Math.toRadians(point2.latitude)
        val lon2 = Math.toRadians(point2.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1

        val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

}
