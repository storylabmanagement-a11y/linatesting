package com.explorefaraya.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.explorefaraya.app.data.model.DashboardItem
import com.explorefaraya.app.data.repository.DashboardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val repository: DashboardRepository = DashboardRepository()
) : ViewModel() {

    private val _items = MutableStateFlow<List<DashboardItem>>(emptyList())
    val items: StateFlow<List<DashboardItem>> = _items.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeItems().collect { _items.value = it }
        }
    }

    fun addItem(title: String, note: String) {
        if (title.isBlank()) return
        viewModelScope.launch {
            repository.addItem(title.trim(), note.trim())
        }
    }

    fun deleteItem(itemId: String) {
        viewModelScope.launch {
            repository.deleteItem(itemId)
        }
    }
}
