package com.idyllic.yandexmaps.ui.dialog

import android.os.Bundle
import android.view.View
import com.idyllic.common.base.BaseBottomSheetDialogFragment
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.DialogLocationBinding

class LocationDialog : BaseBottomSheetDialogFragment(R.layout.dialog_location) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.idyllic.ui_module.R.style.CustomBottomSheetDialogTheme);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = DialogLocationBinding.bind(view)

        binding.textName.text = "Le Grande Plaza Hotel"
        binding.textStreet.text = "Ташкент, ул. Узбекистон Овози, 2"
        binding.textRating.text = "517 оценок"

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    companion object Factory {
        @JvmStatic
        fun newInstance(): LocationDialog = LocationDialog()
    }

}