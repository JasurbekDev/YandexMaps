package com.idyllic.yandexmaps.ui.screen.status.network

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.common.base.BaseFragment
import com.idyllic.common.util.isNetworkConnected
import com.idyllic.core.ktx.backPressedScreen
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.ScreenCheckNetworkBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckNetworkScreen : BaseFragment(R.layout.screen_check_network) {
    override val viewModel: CheckNetworkScreenVM by viewModels()
    private val binding by viewBinding(ScreenCheckNetworkBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUpdate.setOnClickListener {
            if (requireContext().isNetworkConnected()) {
                findNavController().popBackStack()
            }
        }

        backPressedScreen(true) {
            requireActivity().finish()
        }
    }
}