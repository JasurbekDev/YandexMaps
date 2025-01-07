package com.idyllic.yandexmaps.ui.screen.menu.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import by.kirich1409.viewbindingdelegate.viewBinding
import com.idyllic.common.util.safeNavigate
import com.idyllic.core_api.model.UserDto
import com.idyllic.core_api.usecase.SecureStorageManager
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.base.BaseMainFragment
import com.idyllic.yandexmaps.databinding.ScreenMenuBinding
import com.idyllic.yandexmaps.ui.screen.main.MainScreenDirections
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MenuScreen : BaseMainFragment(R.layout.screen_menu), View.OnClickListener {
    override val viewModel: MenuScreenVM by viewModels()
    private val binding by viewBinding(ScreenMenuBinding::bind)
    private var mainNavigation: NavController? = null

    @Inject
    lateinit var secureStorageManager: SecureStorageManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)

        viewModel.apply {
            loginLiveData.observe(viewLifecycleOwner, loginObserver)
        }

        binding.phoneInput.setText("+998974220725")
        binding.passwordInput.setText("12345678")
        binding.loginBtn.setOnClickListener(this)
    }

    private val loginObserver = Observer<UserDto> {
        it.token?.let { token ->
            secureStorageManager.token = token
            val d = MainScreenDirections.actionMainScreenToLinesScreen()
            mainNavigation?.safeNavigate(d)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_btn -> {
                val phoneNumber = binding.phoneInput.text.toString()
                val password = binding.passwordInput.text.toString()
                viewModel.login(phoneNumber, password)
            }
        }
    }
}