package com.idyllic.yandexmaps.ui.screen.menu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.idyllic.core_api.model.LineDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.ItemLineBinding

class LinePagingAdapter(private val listener: Callback) :
    PagingDataAdapter<LineDto, LinePagingAdapter.ViewHolder>(LineDiffUtil) {

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bind(t: LineDto) {
            val binding = ItemLineBinding.bind(view)
            val fromToText = "${t.fromRegion?.name} - ${t.toRegion?.name}"
            binding.fromToTv.text = fromToText
            binding.descriptionTv.text = t.description
            binding.carTv.text = t.userCar?.car?.title
            binding.root.setOnClickListener {
                listener.onLineSelect(t)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(
            inflater.inflate(
                R.layout.item_line, parent, false
            )
        )
    }

    object LineDiffUtil : DiffUtil.ItemCallback<LineDto>() {

        override fun areItemsTheSame(
            oldItem: LineDto, newItem: LineDto
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: LineDto, newItem: LineDto
        ): Boolean {
            return oldItem.id == newItem.id
        }
    }

    interface Callback {
        fun onLineSelect(dto: LineDto)
    }

}