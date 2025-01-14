package com.idyllic.yandexmaps.ui.dialog

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.idyllic.common.base.BaseBottomSheetFullDialogFragment
import com.idyllic.map_api.model.LocationDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.DialogSearchLocationBinding
import com.idyllic.yandexmaps.models.GeoObjectLocation
import com.idyllic.yandexmaps.ui.adapter.list.SearchLocationAdapter
import com.yandex.mapkit.geometry.Point
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchLocationDialog : BaseBottomSheetFullDialogFragment(R.layout.dialog_search_location),
    SearchLocationAdapter.Callback {

    private var binding: DialogSearchLocationBinding? = null
    private var listener: (GeoObjectLocation) -> Unit = {}
    private var adapter: SearchLocationAdapter? = null
    private val viewModel: SearchLocationDialogVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, com.idyllic.ui_module.R.style.CustomBottomSheetDialogTheme)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DialogSearchLocationBinding.bind(view)

        adapter = SearchLocationAdapter(this)
        binding?.recyclerLocation?.adapter = adapter

        viewModel.apply {
            searchResultLiveData.observe(viewLifecycleOwner, searchResultObserver)
        }

        binding?.editSearch?.let { edit ->
            edit.addTextChangedListener {
                if (it.toString().length > 3) {
                    viewModel.searchLocation(it.toString())
                }
            }
        }

        binding?.editSearch?.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding?.editSearch?.text.toString().length > 3) {
                    viewModel.searchLocation(binding?.editSearch?.text.toString())
                }
                return@OnEditorActionListener false
            }
            false
        })

        binding?.editSearch?.requestFocus()
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
    }

    private val searchResultObserver = Observer<List<LocationDto>> {
        adapter?.submitList(it)
    }

    companion object Factory {
        @JvmStatic
        fun newInstance(listener: (GeoObjectLocation) -> Unit): SearchLocationDialog =
            SearchLocationDialog().apply {
                this.listener = listener
            }
    }

    override fun selectItem(t: LocationDto) {
        t.lat?.let { lat ->
            t.lon?.let { lon ->
                val geoObjectLocation = GeoObjectLocation(
                    name = t.name,
                    address = t.street,
                    point = Point(lat, lon)
                )
                listener.invoke(geoObjectLocation)
            }
        }
        dismiss()
    }

}