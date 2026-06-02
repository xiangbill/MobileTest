package com.example.mobiletest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobiletest.model.GenericItem

class HomeViewModel : ViewModel() {

    private val _items = MutableLiveData<List<GenericItem>>(emptyList())
    val items: LiveData<List<GenericItem>> = _items

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    var currentCategory: String = "All"
    var currentPage: Int = 1

    fun setItems(newItems: List<GenericItem>) {
        _items.value = newItems
    }

    fun addItems(moreItems: List<GenericItem>) {
        val currentList = _items.value ?: emptyList()
        _items.value = currentList + moreItems
    }

    fun setLoading(loading: Boolean) {
        _isLoading.value = loading
    }
}
