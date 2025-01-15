package com.idyllic.yandexmaps.ui.screen.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.common.vm.SharedViewModel
import com.idyllic.map_api.model.LocationDto
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.base.BaseMainFragment
import com.idyllic.yandexmaps.databinding.ScreenBookmarksBinding
import com.idyllic.yandexmaps.ui.screen.bookmarks.adapter.BookmarkAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarksScreen : BaseMainFragment(R.layout.screen_bookmarks), BookmarkAdapter.Callback {
    override val viewModel: BookmarksScreenVM by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val binding by viewBinding(ScreenBookmarksBinding::bind)
    private var mainNavigation: NavController? = null
    private var adapter: BookmarkAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        adapter = BookmarkAdapter(this)
        binding.recyclerLocation.adapter = adapter

        lifecycleScope.launch {
            viewModel.bookmarks.collect { data ->
                adapter?.submitList(data)
            }
        }

        val swipeToDeleteCallback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                viewModel.removeBookmark(position)
//                adapter?.notifyItemRemoved(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerLocation)

    }

    override fun selectItem(t: LocationDto) {
        sharedViewModel.showBookmarkOnMap(t)
    }

}