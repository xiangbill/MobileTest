package com.example.mobiletest.ui.home

import android.os.Handler
import android.os.Looper
import com.example.mobiletest.adapter.BannerAdapter
import com.example.mobiletest.base.BaseFragment
import com.example.mobiletest.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val bannerHandler = Handler(Looper.getMainLooper())
    private val bannerRunnable = object : Runnable {
        override fun run() {
            _binding?.bannerViewPager?.let { viewPager ->
                val currentItem = viewPager.currentItem
                val nextItem = if (currentItem == 2) 0 else currentItem + 1
                viewPager.setCurrentItem(nextItem, true)
                bannerHandler.postDelayed(this, 3000)
            }
        }
    }

    private val categories = listOf("All", "Tech", "Lifestyle", "Fashion", "Food")

    override fun initView() {
        if (binding.bannerViewPager.adapter == null) {
            setupBanner()
        }
        if (binding.categoryViewPager.adapter == null) {
            setupCategoryPager()
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
    }

    private fun setupCategoryPager() {
        val adapter = CategoryPagerAdapter(this, categories)
        binding.categoryViewPager.offscreenPageLimit = 5 // 设置足够的缓存，防止切换 tab 时销毁
        binding.categoryViewPager.adapter = adapter
        
        TabLayoutMediator(binding.categoryTabLayout, binding.categoryViewPager) { tab, position ->
            tab.text = categories[position]
        }.attach()
    }

    override fun onResume() {
        super.onResume()
        // 恢复轮播
        bannerHandler.removeCallbacks(bannerRunnable)
        bannerHandler.postDelayed(bannerRunnable, 3000)
    }

    override fun onPause() {
        super.onPause()
        // 停止轮播，防止泄露和 NPE
        bannerHandler.removeCallbacks(bannerRunnable)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bannerHandler.removeCallbacks(bannerRunnable)
    }
}
