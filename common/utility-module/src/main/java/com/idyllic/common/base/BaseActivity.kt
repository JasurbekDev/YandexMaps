package com.idyllic.common.base

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {
    abstract fun changeMode(mode: Boolean)
    abstract fun changeLanguage(language: String)
}