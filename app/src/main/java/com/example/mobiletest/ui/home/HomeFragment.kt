package com.example.mobiletest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mobiletest.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayout

import androidx.recyclerview.widget.LinearLayoutManager
import android.os.Handler
import android.os.Looper
import com.example.mobiletest.adapter.BannerAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.example.mobiletest.adapter.GenericAdapter
import com.example.mobiletest.model.GenericItem
import androidx.fragment.app.viewModels

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private val bannerHandler = Handler(Looper.getMainLooper())
    private val bannerRunnable = object : Runnable {
        override fun run() {
            _binding?.let { b ->
                val currentItem = b.bannerViewPager.currentItem
                val nextItem = if (currentItem == 2) 0 else currentItem + 1
                b.bannerViewPager.setCurrentItem(nextItem, true)
                bannerHandler.postDelayed(this, 3000)
            }
        }
    }

    private var adapter: GenericAdapter? = null
    private var isLoading = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBanner()
        setupCategoryTabs()
        setupRefreshLayout()
        setupRecyclerView()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.items.observe(viewLifecycleOwner) { items ->
            adapter?.setData(items)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            isLoading = loading
            _binding?.swipeRefreshLayout?.isRefreshing = loading
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
        adapter = GenericAdapter(mutableListOf())
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
        
        // 关键改动：如果 ViewModel 里已经有数据了，就不要再触发 refreshData
        if (viewModel.items.value.isNullOrEmpty()) {
            refreshData("All")
        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
