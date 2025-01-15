package com.idyllic.yandexmaps.ui.screen.bookmarks.adapter

import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.setPadding
import com.idyllic.common.base.SuperListAdapter
import com.idyllic.common.util.dpToPx
import com.idyllic.core.ktx.visible
import com.idyllic.map_api.model.LocationDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.ItemBookmarkBinding

class BookmarkAdapter(private val listener: Callback) : SuperListAdapter<LocationDto>(
    R.layout.item_bookmark,
    { oldItem, newItem -> oldItem == newItem },
    { oldItem, newItem -> oldItem == newItem }
) {
    override fun bind(t: LocationDto, view: View, adapterPosition: Int) {
        val binding = ItemBookmarkBinding.bind(view)

        if (t.name.isNullOrEmpty()) {
            binding.textName.text = t.street
        } else {
            binding.textName.text = t.name
            binding.textStreet.text = t.street
        }

        binding.cardBookmark.setOnClickListener {
            listener.selectItem(t)
        }
    }

    interface Callback {
        fun selectItem(t: LocationDto)
    }
}