package com.idyllic.yandexmaps.ui.dialog

import android.os.Bundle
import android.view.View
import com.idyllic.common.base.BaseBottomSheetDialogFragment
import com.idyllic.common.base.BaseBottomSheetFullDialogFragment
import com.idyllic.core.ktx.toast
import com.idyllic.map_api.model.LocationDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.DialogSearchLocationBinding
import com.idyllic.yandexmaps.ui.adapter.list.SearchLocationAdapter

class SearchLocationDialog : BaseBottomSheetFullDialogFragment(R.layout.dialog_search_location),
    SearchLocationAdapter.Callback {

    private var binding: DialogSearchLocationBinding? = null
    private var listener: Callback? = null
    private var adapter: SearchLocationAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.idyllic.ui_module.R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogSearchLocationBinding.bind(view)

        adapter = SearchLocationAdapter(this)
        binding?.recyclerLocation?.adapter = adapter

        adapter?.submitList(
            arrayListOf(
                LocationDto(
                    name = "Abc",
                    street = "!@#",
                    distance = "123"
                ),
                LocationDto(
                    name = "Abc2",
                    street = "!@#",
                    distance = "123"
                ),
//                LocationDto(
//                    name = "Abc3",
//                    street = "!@#",
//                    distance = "123"
//                ),
//                LocationDto(
//                    name = "Abc4",
//                    street = "!@#",
//                    distance = "123"
//                ),
//                LocationDto(
//                    name = "Abc5",
//                    street = "!@#",
//                    distance = "123"
//                )
            )
        )
    }

    companion object Factory {
        @JvmStatic
        fun newInstance(): SearchLocationDialog = SearchLocationDialog()
    }

    interface Callback {
        fun dismiss()
    }

    override fun selectItem(t: LocationDto) {
        toast(t.name)
    }

}