package com.idyllic.yandexmaps.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.idyllic.common.vm.SingleLiveEvent
import com.idyllic.core_imp.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class BaseMainVM : ViewModel() {
    private val _globalErrorLiveData = SingleLiveEvent<String>()
    val globalErrorLiveData: LiveData<String> = _globalErrorLiveData
    private val _internalServerErrorLiveData = SingleLiveEvent<Unit>()
    val internalServerErrorLiveData: LiveData<Unit> = _internalServerErrorLiveData

    private val _logoutScreenLiveData = SingleLiveEvent<Unit>()
    val logoutScreenLiveData: LiveData<Unit> = _logoutScreenLiveData

    private val _updateAppLiveData = SingleLiveEvent<Unit>()
    val updateAppLiveData: LiveData<Unit> = _updateAppLiveData

    private val _blockUserLiveData = SingleLiveEvent<String>()
    val blockUserLiveData: LiveData<String> = _blockUserLiveData

    private val _baseGlobalLoadingLiveData = SingleLiveEvent<Boolean>()
    val baseGlobalLoadingLiveData: LiveData<Boolean> = _baseGlobalLoadingLiveData

    private val _ddosLiveData = SingleLiveEvent<Unit>()
    val ddosLiveData: LiveData<Unit> = _ddosLiveData

    private val _technicalBreakLiveData = SingleLiveEvent<Unit>()
    val technicalBreakLiveData: LiveData<Unit> = _technicalBreakLiveData

    private val _errorToServerLiveData = SingleLiveEvent<String>()
    val errorToServerLiveData: LiveData<String> = _errorToServerLiveData

    private val _errorServerErrorLiveData = SingleLiveEvent<Unit>()
    val errorServerErrorLiveData: LiveData<Unit> = _errorServerErrorLiveData

    private val _navigateToLoginScreenLiveData = SingleLiveEvent<Unit>()
    val navigateToLoginScreenLiveData: LiveData<Unit> = _navigateToLoginScreenLiveData

    private val _deviceNotActivatedLiveData = SingleLiveEvent<Unit>()
    val deviceNotActivatedLiveData: LiveData<Unit> = _deviceNotActivatedLiveData


    protected fun launchVM(
        context: CoroutineContext = EmptyCoroutineContext,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit
    ): Job = viewModelScope.launch(context, start, block)

    protected fun globalError(error: Throwable) {
        when (error) {
            is InternalServerError -> {
                _internalServerErrorLiveData.value = Unit
            }
            is TokenWrongException -> {
                _logoutScreenLiveData.value = Unit
            }
            is DdosServerError -> {
//                _globalErrorLiveData.value = error.message
                _ddosLiveData.value = Unit
            }
            is TechnicalBreakServerError -> {
//                _globalErrorLiveData.value = error.message
                _technicalBreakLiveData.value = Unit
            }
            is AppUpdateException -> {
                _updateAppLiveData.value = Unit
            }

            is BlockUserException -> {
                _blockUserLiveData.value = error.message
            }

            is ErrorToServerError -> {
                _errorToServerLiveData.value = error.message
            }

            is UnknownHostException -> {
                _errorServerErrorLiveData.value = Unit
            }

            is ServerException -> {
                _internalServerErrorLiveData.value = Unit
            }

            is TimeoutError -> {
                _internalServerErrorLiveData.value = Unit
            }

            is RefreshTokenExpired -> {
                _navigateToLoginScreenLiveData.value = Unit
            }

            is DeviceNotActivated -> {
                _deviceNotActivatedLiveData.value = Unit
            }

            else -> {
                _internalServerErrorLiveData.value = Unit
            }
        }
    }

    protected fun showGlobalLoading() {
        _baseGlobalLoadingLiveData.value = true
    }

    protected fun hideGlobalLoading() {
        _baseGlobalLoadingLiveData.value = false
    }
}
