package com.example.search_image.presentation.ui.homescreen.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.search_image.common.Resource
import com.example.search_image.common.utils.NetworkUtil
import com.example.search_image.domain.repository.SearchRepository
import com.example.search_image.presentation.ui.common.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val repository: SearchRepository,
    private val networkUtils: NetworkUtil
) :
    BaseViewModel() {
    var state by mutableStateOf(SearchListingState())
        private set
    private var searchJob: Job? = null

    init {
        getSearchListing(query = initialQuery)
        observeNetworkConnection(networkUtils)
    }

    private fun getSearchListing(
        query: String = state.searchQuery.lowercase(),
    ) {
        viewModelScope.launch {
            repository
                .getSearchResults(searchQuery = query)
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            result.data?.let { listings ->
                                state = state.copy(
                                    searchLists = listings
                                )
                            }
                        }
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            state = state.copy(isLoading = result.isLoading)
                        }
                    }
                }
        }
    }


    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(searchTimeDelay)
                    getSearchListing()
                }
            }
            else -> {}
        }
    }

    override fun onConnectionBack() {
        getSearchListing(
            state.searchQuery.ifEmpty { initialQuery }
        )
    }

    companion object {
        const val initialQuery = "fruits"
        const val searchTimeDelay = 500L
        const val connectionBackMessageTimeout = 2000L
    }
}