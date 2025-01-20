package com.idyllic.yandexmaps.ui.dialog

import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.idyllic.common.base.BaseInteractableBottomSheetDialogFragment
import com.idyllic.common.util.customGetParcelable
import com.idyllic.common.util.customGetSerializable
import com.idyllic.core.ktx.gone
import com.idyllic.core.ktx.timber
import com.idyllic.core.ktx.visible
import com.idyllic.map_api.model.LocationDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.DialogLocationBinding
import com.idyllic.yandexmaps.models.GeoObjectLocation

const val KEY_GEO_OBJECT_LOCATION = "KEY_GEO_OBJECT_LOCATION"
const val KEY_LOCATION_DIALOG_CALLBACK = "KEY_LOCATION_DIALOG_CALLBACK"

class LocationDialogInteractable : BaseInteractableBottomSheetDialogFragment(R.layout.dialog_location) {

    private var geoObjectLocation: GeoObjectLocation? = null
    private var binding: DialogLocationBinding? = null

    private var imageStar1: AppCompatImageView? = null
    private var imageStar2: AppCompatImageView? = null
    private var imageStar3: AppCompatImageView? = null
    private var imageStar4: AppCompatImageView? = null
    private var imageStar5: AppCompatImageView? = null

    private val imageStarList: MutableList<AppCompatImageView?> = arrayListOf()

    private var callback: Callback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.idyllic.ui_module.R.style.CustomBottomSheetDialogTheme)

        arguments?.let {
            geoObjectLocation = it.customGetSerializable(KEY_GEO_OBJECT_LOCATION) as GeoObjectLocation?
            callback = it.customGetParcelable(KEY_LOCATION_DIALOG_CALLBACK) as Callback?
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogLocationBinding.bind(view)

        updateContent(geoObjectLocation)

        binding?.btnClose?.setOnClickListener {
            dismiss()
        }

        binding?.btnAddToBookmarks?.setOnClickListener {
            geoObjectLocation?.let {
                callback?.onBookmarkClick(it)
            }
        }
    }

    fun setGeoObjectLocation(geoObjectLocation: GeoObjectLocation?) {
        this.geoObjectLocation = geoObjectLocation
        updateContent(geoObjectLocation)
    }

    private fun updateContent(geoObjectLocation: GeoObjectLocation?) {
        collectImageStars()
        geoObjectLocation?.name?.let {
            binding?.textStreet?.visible()
            binding?.textName?.text = geoObjectLocation.name
            binding?.textStreet?.text = geoObjectLocation.address
        } ?: run {
            binding?.textName?.text = geoObjectLocation?.address
            binding?.textStreet?.gone()
        }
        geoObjectLocation?.rating?.let {
            showAllStars()
            binding?.textReviews?.visible()
            binding?.textReviews?.text = geoObjectLocation.reviews.toString()
        } ?: run {
            hideAllStars()
            binding?.textReviews?.gone()
        }

        unselectAllStars()

        geoObjectLocation?.rating?.let { rate ->
            timber("RATEEEE: $rate")
            for (i in 0 until rate) {
                imageStarList[i]?.setImageResource(com.idyllic.ui_module.R.drawable.ic_star_selected)
            }
        }
    }

    private fun unselectAllStars() {
        for (i in 0 until 5) {
            imageStarList[i]?.setImageResource(com.idyllic.ui_module.R.drawable.ic_star_unselected)
        }
    }

    private fun collectImageStars() {
        imageStar1 = binding?.imageStar1
        imageStar2 = binding?.imageStar2
        imageStar3 = binding?.imageStar3
        imageStar4 = binding?.imageStar4
        imageStar5 = binding?.imageStar5

        imageStarList.add(imageStar1)
        imageStarList.add(imageStar2)
        imageStarList.add(imageStar3)
        imageStarList.add(imageStar4)
        imageStarList.add(imageStar5)

        unselectAllStars()
    }

    private fun showAllStars() {
        binding?.imageStar1?.visible()
        binding?.imageStar2?.visible()
        binding?.imageStar3?.visible()
        binding?.imageStar4?.visible()
        binding?.imageStar5?.visible()
    }

    private fun hideAllStars() {
        binding?.imageStar1?.gone()
        binding?.imageStar2?.gone()
        binding?.imageStar3?.gone()
        binding?.imageStar4?.gone()
        binding?.imageStar5?.gone()
    }

    companion object Factory {
        @JvmStatic
        fun newInstance(
            geoObjectLocation: GeoObjectLocation?,
            bookmarkListener: Callback
        ): LocationDialogInteractable = LocationDialogInteractable().apply {
            arguments = Bundle().apply {
                putSerializable(KEY_GEO_OBJECT_LOCATION, geoObjectLocation)
                putParcelable(KEY_LOCATION_DIALOG_CALLBACK, bookmarkListener)
            }
        }
    }

    interface Callback : Parcelable {
        fun onBookmarkClick(location: GeoObjectLocation)
    }

}