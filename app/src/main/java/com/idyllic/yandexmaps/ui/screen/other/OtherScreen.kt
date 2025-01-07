package com.idyllic.yandexmaps.ui.screen.other

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.common.base.BaseFragment
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.ScreenOtherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OtherScreen : BaseFragment(R.layout.screen_other) {
    override val viewModel: OtherScreenVM by viewModels()
    private val binding by viewBinding(ScreenOtherBinding::bind)
}