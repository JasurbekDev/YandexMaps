package com.idyllic.yandexmaps.ui.screen.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.common.util.setFromScreenMaterialSharedAxisZTransition
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.base.BaseMainFragment
import com.idyllic.yandexmaps.databinding.ScreenBookmarksBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookmarksScreen : BaseMainFragment(R.layout.screen_bookmarks) {
    override val viewModel: HomeScreenVM by viewModels()
    private val binding by viewBinding(ScreenBookmarksBinding::bind)
    private var mainNavigation: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        viewModel.apply {

        }

    }

}