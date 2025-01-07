package com.idyllic.yandexmaps.ui.adapter.list

import android.view.View
import com.idyllic.common.base.SuperListAdapter
import com.idyllic.core_api.model.LineDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.ItemLineBinding

class LineAdapter(private val listener: Callback) : SuperListAdapter<LineDto>(
    R.layout.item_line,
    { oldItem, newItem -> oldItem == newItem },
    { oldItem, newItem -> oldItem == newItem }
) {
    override fun bind(t: LineDto, view: View, adapterPosition: Int) {
        val binding = ItemLineBinding.bind(view)
        val fromToText = "${t.fromRegion?.name} - ${t.toRegion?.name}"
        binding.fromToTv.text = fromToText
        binding.descriptionTv.text = t.description
        binding.carTv.text = t.userCar?.car?.title
        binding.root.setOnClickListener {
            listener.selectItem(t)
        }
    }

    interface Callback {
        fun selectItem(t: LineDto)
    }
}