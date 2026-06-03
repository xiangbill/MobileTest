package com.example.mobiletest.ui.home

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletest.adapter.BannerAdapter
import com.example.mobiletest.adapter.GenericAdapter
import com.example.mobiletest.base.BaseFragment
import com.example.mobiletest.databinding.FragmentHomeBinding
import com.example.mobiletest.model.GenericItem
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

import android.content.Intent
import com.example.mobiletest.ui.detail.DetailActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel: HomeViewModel by viewModels()

    private val bannerHandler = Handler(Looper.getMainLooper())
    private val bannerRunnable = object : Runnable {
        override fun run() {
            val currentItem = binding.bannerViewPager.currentItem
            val nextItem = if (currentItem == 2) 0 else currentItem + 1
            binding.bannerViewPager.setCurrentItem(nextItem, true)
            bannerHandler.postDelayed(this, 3000)
        }
    }

    private var adapter: GenericAdapter? = null
    private var isLoading = false

    override fun initView() {
        setupBanner()
        setupCategoryTabs()
        setupRefreshLayout()
        setupRecyclerView()
    }

    override fun initData() {
        observeViewModel()
        if (viewModel.items.value.isNullOrEmpty()) {
            refreshData("All")
        }
    }

    private fun observeViewModel() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter?.setData(items)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            isLoading = loading
            binding.swipeRefreshLayout.isRefreshing = loading
        }
    }

    private fun setupRefreshLayout() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData(viewModel.currentCategory)
        }
    }

    private fun setupBanner() {
        val bannerImages = listOf(
            "https://picsum.photos/id/10/800/300",
            "https://picsum.photos/id/20/800/300",
            "https://picsum.photos/id/30/800/300"
        )
        binding.bannerViewPager.adapter = BannerAdapter(bannerImages)
        
        TabLayoutMediator(binding.bannerIndicator, binding.bannerViewPager) { _, _ -> }.attach()
        
        bannerHandler.postDelayed(bannerRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        bannerHandler.removeCallbacks(bannerRunnable)
    }

    override fun onResume() {
        super.onResume()
        bannerHandler.postDelayed(bannerRunnable, 3000)
    }

    private fun setupCategoryTabs() {
        val categories = listOf("All", "Tech", "Lifestyle", "Fashion", "Food")
        categories.forEach { category ->
            binding.categoryTabLayout.addTab(binding.categoryTabLayout.newTab().setText(category))
        }

        binding.categoryTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                refreshData(tab?.text.toString())
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupRecyclerView() {
        adapter = GenericAdapter(mutableListOf()) { item ->
            val intent = Intent(requireContext(), DetailActivity::class.java).apply {
                putExtra("title", item.title)
                putExtra("description", item.description)
                putExtra("imageUrl", item.imageUrl)
            }
            startActivity(intent)
        }
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.homeRecyclerView.adapter = adapter
        
        binding.homeRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
    }

    private fun refreshData(category: String) {
        viewModel.currentCategory = category
        viewModel.currentPage = 1
        viewModel.setLoading(true)
        
        // Simulate network delay
        Handler(Looper.getMainLooper()).postDelayed({
            val dummyData = List(10) { i ->
                GenericItem(i, "$category Item $i", "Description for $category item $i")
            }
            viewModel.setItems(dummyData)
            viewModel.setLoading(false)
        }, 1500)
    }

    private fun loadMoreData() {
        viewModel.setLoading(true)
        viewModel.currentPage++
        
        // Simulate network delay
        Handler(Looper.getMainLooper()).postDelayed({
            val nextStart = (viewModel.currentPage - 1) * 10
            val dummyData = List(10) { i ->
                val index = nextStart + i
                GenericItem(index, "${viewModel.currentCategory} Item $index", "Description for ${viewModel.currentCategory} item $index")
            }
            viewModel.addItems(dummyData)
            viewModel.setLoading(false)
        }, 1500)
    }
}
