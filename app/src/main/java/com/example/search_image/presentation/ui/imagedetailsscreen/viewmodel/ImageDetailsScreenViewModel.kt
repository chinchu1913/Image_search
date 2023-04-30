package com.example.search_image.presentation.ui.imagedetailsscreen.viewmodel

import com.example.search_image.common.utils.NetworkUtil
import com.example.search_image.presentation.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ImageDetailsScreenViewModel @Inject constructor(
    networkUtils: NetworkUtil
) : BaseViewModel() {
    init {
        observeNetworkConnection(networkUtils)
    }
}