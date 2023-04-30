package com.example.search_image.presentation.ui.homescreen.viewmodel


sealed class SearchEvent {
    data class OnSearchQueryChange(val query: String) : SearchEvent()
}