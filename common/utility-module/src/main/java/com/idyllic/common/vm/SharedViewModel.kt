package com.idyllic.common.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SharedViewModel @Inject constructor() : ViewModel() {

    private val _finishActivityLiveData = SingleLiveEvent<Unit>()
    val finishActivityLiveData: LiveData<Unit> = _finishActivityLiveData

    fun finishActivity() {
        _finishActivityLiveData.value = Unit
    }

}