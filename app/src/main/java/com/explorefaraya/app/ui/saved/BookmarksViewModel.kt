package com.explorefaraya.app.ui.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explorefaraya.app.data.repository.Bookmarks
import com.explorefaraya.app.data.repository.BookmarksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookmarksViewModel(
    private val repository: BookmarksRepository = BookmarksRepository()
) : ViewModel() {

    private val _bookmarks = MutableStateFlow(Bookmarks())
    val bookmarks: StateFlow<Bookmarks> = _bookmarks.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeBookmarks().collect { _bookmarks.value = it }
        }
    }

    fun toggleEvent(eventId: String) {
        val isBookmarked = eventId in _bookmarks.value.eventIds
        viewModelScope.launch { repository.toggleEventBookmark(eventId, isBookmarked) }
    }

    fun toggleSite(siteId: String) {
        val isBookmarked = siteId in _bookmarks.value.siteIds
        viewModelScope.launch { repository.toggleSiteBookmark(siteId, isBookmarked) }
    }
}
