package com.example.mobiletest.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobiletest.model.GenericItem

class CategoryViewModel : ViewModel() {
    // 使用 Map 存储每个分类的数据，Key 是 category 名称
    private val categoryDataMap = mutableMapOf<String, MutableLiveData<List<GenericItem>>>()
    
    // 存储每个分类的当前页码
    private val pageMap = mutableMapOf<String, Int>()

    fun getLiveData(category: String): MutableLiveData<List<GenericItem>> {
        return categoryDataMap.getOrPut(category) { MutableLiveData(emptyList()) }
    }

    fun getPage(category: String): Int {
        return pageMap.getOrDefault(category, 1)
    }

    fun setPage(category: String, page: Int) {
        pageMap[category] = page
    }

    fun setData(category: String, data: List<GenericItem>) {
        getLiveData(category).value = data
    }

    fun addData(category: String, newData: List<GenericItem>) {
        val currentList = getLiveData(category).value ?: emptyList()
        getLiveData(category).value = currentList + newData
    }
}
