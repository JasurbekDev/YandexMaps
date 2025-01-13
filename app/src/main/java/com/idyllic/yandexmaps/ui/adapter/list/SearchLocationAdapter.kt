package com.idyllic.yandexmaps.ui.adapter.list

import android.view.View
import com.idyllic.common.base.SuperListAdapter
import com.idyllic.map_api.model.LocationDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.ItemLocationBinding

class SearchLocationAdapter(private val listener: Callback) : SuperListAdapter<LocationDto>(
    R.layout.item_location,
    { oldItem, newItem -> oldItem == newItem },
    { oldItem, newItem -> oldItem == newItem }
) {
    override fun bind(t: LocationDto, view: View, adapterPosition: Int) {
        val binding = ItemLocationBinding.bind(view)
        binding.textName.text = t.name
        binding.textStreet.text = t.street
        binding.textDistance.text = t.distance
        binding.root.setOnClickListener {
            listener.selectItem(t)
        }
    }

    interface Callback {
        fun selectItem(t: LocationDto)
    }
}