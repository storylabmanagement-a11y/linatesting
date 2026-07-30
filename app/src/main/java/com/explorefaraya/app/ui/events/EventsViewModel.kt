package com.explorefaraya.app.ui.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explorefaraya.app.data.repository.BookmarksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventsViewModel(
    private val repository: BookmarksRepository = BookmarksRepository()
) : ViewModel() {

    private val _bookmarkedIds = MutableStateFlow<Set<String>>(emptySet())
    val bookmarkedIds: StateFlow<Set<String>> = _bookmarkedIds.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeBookmarkedEventIds().collect { _bookmarkedIds.value = it }
        }
    }

    fun toggleBookmark(eventId: String) {
        val isBookmarked = eventId in _bookmarkedIds.value
        viewModelScope.launch {
            repository.toggleBookmark(eventId, isBookmarked)
        }
    }
}
