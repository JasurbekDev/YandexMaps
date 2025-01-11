package com.idyllic.map_imp.source

import com.idyllic.core_api.model.ResourceUI
import com.idyllic.map_api.source.MapDataSource
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

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun getAddressFromCoordinates(lat: Double, lon: Double): String = suspendCancellableCoroutine { continuation ->
        val searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)

        searchManager.submit(
            Point(lat, lon),
            0,
            SearchOptions(),
            object : Session.SearchListener {
                override fun onSearchResponse(response: Response) {
                    val address = response.collection.children.firstOrNull()?.obj?.name
                    val desc = response.collection.children.firstOrNull()?.obj?.descriptionText ?: ""

                    val descParts = desc.split(",").map { it.trim() }.reversed()
                    val descWithoutLast = if (descParts.size > 1) descParts.drop(1).joinToString(", ") else ""

                    val fullAddress = if (descWithoutLast.isNotEmpty()) "$descWithoutLast, $address" else address ?: ""
                    continuation.resume(fullAddress) {}
                }

                override fun onSearchError(error: Error) {
                    Timber.e("ReverseGeocoding", "Error: $error")
                    continuation.resumeWithException(Exception(error.toString()))
                }
            }
        )
    }

}
