package com.example.search_image.presentation.ui.common.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.search_image.common.utils.NetworkUtil
import com.example.search_image.presentation.ui.homescreen.viewmodel.HomeScreenViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private var lastConnectionStatus = true
    var baseState by mutableStateOf(BaseState())
        private set

    open fun onConnectionBack() {}

    protected fun observeNetworkConnection(networkUtils: NetworkUtil) {
        viewModelScope.launch {
            networkUtils.getNetworkLiveData()
            networkUtils.getNetworkLiveData().asFlow().collect { isConnected ->
                //show the connection error is the connection status is disconnected
                baseState = baseState.copy(showNetworkUnavailable = !isConnected)
                //show the connection success message if the connection if disconnected and reconnected back.
                val isConnectionIsBack = !lastConnectionStatus && isConnected
                if (isConnectionIsBack) {
                    baseState = baseState.copy(showNetworkConnected = true)
                    delay(HomeScreenViewModel.connectionBackMessageTimeout)
                    baseState = baseState.copy(showNetworkConnected = false)
                    onConnectionBack()
                }
                lastConnectionStatus = isConnected
            }
        }

    }

}