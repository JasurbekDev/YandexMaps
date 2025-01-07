package com.idyllic.yandexmaps.ui.screen.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.idyllic.core_api.model.ProductDto
import com.idyllic.core_api.model.ResourceUI
import com.idyllic.core_api.usecase.GetProductUseCase
import com.idyllic.yandexmaps.base.BaseMainVM
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeScreenVM @Inject constructor(
    private val getProductUseCase: GetProductUseCase
) : BaseMainVM() {

    private val _productLiveData = MutableLiveData<ProductDto>()
    val productLiveData: LiveData<ProductDto> = _productLiveData

    fun getProduct(id: Int) {
        showGlobalLoading()
        launchVM {
            getProductUseCase.invoke(id).collect {
                when (it) {
                    is ResourceUI.Error -> {
                        globalError(it.error)
                    }

                    is ResourceUI.Success -> {
                        val data = it.data
                        _productLiveData.value = data
                    }
                }
                hideGlobalLoading()
            }
        }
    }
}