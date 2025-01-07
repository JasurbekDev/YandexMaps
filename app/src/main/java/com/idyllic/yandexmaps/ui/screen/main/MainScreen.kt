package com.idyllic.yandexmaps.ui.screen.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.common.base.BaseFragment
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.ScreenMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainScreen : BaseFragment(R.layout.screen_main) {
    override val viewModel: MainScreenVM by viewModels()
    private val binding by viewBinding(ScreenMainBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = Navigation.findNavController(
            requireActivity(), R.id.nav_host_screen
        )

        binding.bottomNavigation.setupWithNavController(navController)
    }
}