package com.idyllic.yandexmaps.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.idyllic.common.base.BaseActivity
import com.idyllic.common.vm.SharedViewModel
import com.idyllic.core.ktx.visible
import com.idyllic.yandexmaps.BuildConfig
import com.idyllic.yandexmaps.R
import com.idyllic.yandexmaps.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityVM by viewModels()
    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun changeMode(mode: Boolean) {
        if (BuildConfig.DEBUG) {
            findViewById<View>(R.id.layout_type_url).visible(mode)
        }
    }

    override fun changeLanguage(language: String) {
        setLocale(language)
    }

    private lateinit var locale: Locale
    private var currentLanguage = "en"
    private var currentLang: String? = null

    private fun setLocale(localeName: String) {
        if (localeName != currentLanguage) {
            locale = Locale(localeName)
            val res = resources
            val dm = res.displayMetrics
            val conf = res.configuration
            conf.locale = locale
            res.updateConfiguration(conf, dm)
            val refresh = Intent(
                this, MainActivity::class.java
            )
            refresh.putExtra(currentLang, localeName)
            startActivity(refresh)
        } else {
            Toast.makeText(
                this, "Language, , already, , selected)!", Toast.LENGTH_SHORT
            ).show()
        }
    }
}