package com.example.mobiletest.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mobiletest.base.BaseViewModel
import com.example.mobiletest.model.GenericItem

class HomeViewModel : BaseViewModel() {

    private val _items = MutableLiveData<List<GenericItem>>(emptyList())
    val items: LiveData<List<GenericItem>> = _items

    var currentCategory: String = "All"
    var currentPage: Int = 1

    fun setItems(newItems: List<GenericItem>) {
        _items.value = newItems
    }

    fun addItems(moreItems: List<GenericItem>) {
        val currentList = _items.value ?: emptyList()
        _items.value = currentList + moreItems
    }
}
