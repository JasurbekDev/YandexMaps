package com.idyllic.yandexmaps.ui.screen.account

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.common.base.BaseFragment
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.ScreenProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileScreen : BaseFragment(R.layout.screen_profile) {
    override val viewModel: AccountScreenVM by viewModels()
    private val binding by viewBinding(ScreenProfileBinding::bind)
}