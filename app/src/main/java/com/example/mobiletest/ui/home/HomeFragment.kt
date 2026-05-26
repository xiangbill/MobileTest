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
import androidx.viewpager2.widget.ViewPager2
import com.example.mobiletest.adapter.BannerAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.example.mobiletest.adapter.GenericAdapter
import com.example.mobiletest.model.GenericItem

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
            val currentItem = binding.bannerViewPager.currentItem
            val nextItem = if (currentItem == 2) 0 else currentItem + 1
            binding.bannerViewPager.setCurrentItem(nextItem, true)
            bannerHandler.postDelayed(this, 3000)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBanner()
        setupCategoryTabs()
        setupRecyclerView()
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
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(context)
        refreshData("All")
    }

    private fun refreshData(category: String) {
        val dummyData = List(10) { i ->
            GenericItem(i, "$category Item $i", "Description for $category item $i")
        }
        binding.homeRecyclerView.adapter = GenericAdapter(dummyData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
