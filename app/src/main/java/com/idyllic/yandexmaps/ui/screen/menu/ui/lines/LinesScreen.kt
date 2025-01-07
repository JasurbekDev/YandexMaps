package com.idyllic.yandexmaps.ui.screen.menu.ui.lines

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.common.base.BaseFragment
import com.idyllic.core.ktx.toast
import com.idyllic.core.ktx.visible
import com.idyllic.core_api.model.LineDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.ScreenLinesBinding
import com.idyllic.yandexmaps.ui.adapter.list.LineAdapter
import com.idyllic.yandexmaps.ui.screen.menu.adapter.LinePagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LinesScreen : BaseFragment(R.layout.screen_lines), View.OnClickListener,
    LinePagingAdapter.Callback, LineAdapter.Callback {
    override val viewModel: LinesScreenVM by viewModels()
    private val binding by viewBinding(ScreenLinesBinding::bind)

    private var linePagingAdapter: LinePagingAdapter? = null
    private var lineListAdapter: LineAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linePagingAdapter = LinePagingAdapter(this)
        lineListAdapter = LineAdapter(this)
        binding.recyclerLine.adapter = linePagingAdapter

        viewModel.apply {
            linesLiveData.observe(viewLifecycleOwner, linesObserver)
        }

        lifecycleScope.launch {
            viewModel.linePager?.collectLatest {
                linePagingAdapter?.submitData(it)
            }
        }
        linePagingAdapter?.addLoadStateListener {
            when (it.refresh) {
                is androidx.paging.LoadState.Loading -> {
                    binding.progressBar.visible(true)
                }

                is androidx.paging.LoadState.Error -> {
                    binding.progressBar.visible(false)
                }

                is androidx.paging.LoadState.NotLoading -> {
                    binding.progressBar.visible(false)
                }
            }
        }
    }

    private val linesObserver = Observer<List<LineDto>?> {
        lineListAdapter?.submitList(it)
    }

    override fun onClick(v: View?) {
        when (v?.id) {

        }
    }

    override fun onLineSelect(dto: LineDto) {

    }

    override fun selectItem(t: LineDto) {
        toast(t.id)
    }
}