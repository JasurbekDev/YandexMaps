package com.idyllic.yandexmaps.ui.dialog

import android.os.Bundle
import android.view.View
import com.idyllic.common.base.BaseBottomSheetDialogFragment
import com.idyllic.core.ktx.gone
import com.idyllic.core.ktx.visible
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.DialogLocationBinding
import com.idyllic.yandexmaps.models.GeoObjectLocation

class LocationDialog : BaseBottomSheetDialogFragment(R.layout.dialog_location) {

    private var geoObjectLocation: GeoObjectLocation? = null
    private var binding: DialogLocationBinding? = null
    private var listener: Callback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.idyllic.ui_module.R.style.CustomBottomSheetDialogTheme);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DialogLocationBinding.bind(view)

        updateContent(geoObjectLocation)

        binding?.btnClose?.setOnClickListener {
            dismiss()
        }
    }

    fun setGeoObjectLocation(geoObjectLocation: GeoObjectLocation?) {
        this.geoObjectLocation = geoObjectLocation
        updateContent(geoObjectLocation)
    }

    private fun updateContent(geoObjectLocation: GeoObjectLocation?) {
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
            geoObjectLocation: GeoObjectLocation?
        ): LocationDialog = LocationDialog().apply {
            this.geoObjectLocation = geoObjectLocation
        }
    }

    interface Callback {
        fun dismiss()
    }

}