package com.example.search_image.presentation.ui.homescreen.viewmodel

import com.example.search_image.domain.entities.Search

data class SearchListingState(
    val searchLists: List<Search> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = ""
)