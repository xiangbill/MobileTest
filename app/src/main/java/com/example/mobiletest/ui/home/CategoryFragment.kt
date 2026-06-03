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
    
    private val viewModel: CategoryViewModel by lazy {
        androidx.lifecycle.ViewModelProvider(requireActivity())[CategoryViewModel::class.java]
    }

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
        
        // 观察数据变化
        viewModel.getLiveData(category).observe(viewLifecycleOwner) { items ->
            if (items.isNotEmpty()) {
                adapter?.setData(items)
            }
        }
        
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
        // 如果数据为空，才进行加载
        if (viewModel.getLiveData(category).value.isNullOrEmpty()) {
            refreshData()
        }
    }

    private fun refreshData() {
        viewModel.setPage(category, 1)
        isLoading = true
        binding.swipeRefreshLayout.isRefreshing = true
        
        Handler(Looper.getMainLooper()).postDelayed({
            val dummyData = List(10) { i ->
                GenericItem(i, "$category Item $i", "Description for $category item $i")
            }
            viewModel.setData(category, dummyData)
            isLoading = false
            binding.swipeRefreshLayout.isRefreshing = false
        }, 1000)
    }

    private fun loadMoreData() {
        isLoading = true
        val nextPage = viewModel.getPage(category) + 1
        viewModel.setPage(category, nextPage)
        
        Handler(Looper.getMainLooper()).postDelayed({
            val nextStart = (nextPage - 1) * 10
            val dummyData = List(10) { i ->
                val index = nextStart + i
                GenericItem(index, "$category Item $index", "Description for $category item $index")
            }
            viewModel.addData(category, dummyData)
            isLoading = false
        }, 1000)
    }
}
