package com.example.mobiletest.ui.home

import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletest.adapter.GenericAdapter
import com.example.mobiletest.base.BaseFragment
import com.example.mobiletest.databinding.FragmentCategoryBinding
import com.example.mobiletest.model.GenericItem
import com.example.mobiletest.ui.detail.DetailActivity

class CategoryFragment : BaseFragment<FragmentCategoryBinding>(FragmentCategoryBinding::inflate) {

    private var adapter: GenericAdapter? = null
    private var category: String = "All"
    private var isLoading = false
    private var currentPage = 1

    companion object {
        private const val ARG_CATEGORY = "arg_category"
        
        fun newInstance(category: String): CategoryFragment {
            return CategoryFragment().apply {
                arguments = android.os.Bundle().apply {
                    putString(ARG_CATEGORY, category)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: android.os.Bundle?) {
        super.onCreate(savedInstanceState)
        category = arguments?.getString(ARG_CATEGORY) ?: "All"
    }

    override fun initView() {
        adapter = GenericAdapter(mutableListOf()) { item ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("title", item.title)
                putExtra("description", item.description)
                putExtra("imageUrl", item.imageUrl)
            }
            startActivity(intent)
        }
        binding.categoryRecyclerView.adapter = adapter
        
        binding.categoryRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
                
                if (!isLoading && totalItemCount <= (lastVisibleItem + 2)) {
                    loadMoreData()
                }
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    override fun initData() {
        // 由于使用了 show/hide 模式，只有第一次创建视图时会执行 initData
        // 后续切换 Tab 不会触发 onViewCreated，因此不会重复刷新
        refreshData()
    }

    private fun refreshData() {
        currentPage = 1
        isLoading = true
        binding.swipeRefreshLayout.isRefreshing = true
        
        Handler(Looper.getMainLooper()).postDelayed({
            val dummyData = List(10) { i ->
                GenericItem(i, "$category Item $i", "Description for $category item $i")
            }
            adapter?.setData(dummyData)
            isLoading = false
            _binding?.swipeRefreshLayout?.isRefreshing = false
        }, 1000)
    }

    private fun loadMoreData() {
        isLoading = true
        currentPage++
        
        Handler(Looper.getMainLooper()).postDelayed({
            val nextStart = (currentPage - 1) * 10
            val dummyData = List(10) { i ->
                val index = nextStart + i
                GenericItem(index, "$category Item $index", "Description for $category item $index")
            }
            adapter?.addData(dummyData)
            isLoading = false
        }, 1000)
    }
}
