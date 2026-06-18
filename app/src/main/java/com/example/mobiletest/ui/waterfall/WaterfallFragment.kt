package com.example.mobiletest.ui.waterfall

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mobiletest.MobileApplication
import com.example.mobiletest.adapter.WaterfallAdapter
import com.example.mobiletest.base.BaseFragment
import com.example.mobiletest.databinding.FragmentWaterfallBinding
import com.example.mobiletest.model.GenericItem
import com.example.mobiletest.viewModel.WaterfallViewModel
import com.google.android.material.chip.Chip
import kotlin.random.Random

class WaterfallFragment :
    BaseFragment<FragmentWaterfallBinding>(FragmentWaterfallBinding::inflate) {

    private val viewModel: WaterfallViewModel by viewModels()

    override fun initView() {
        setupLabels()
        setupWaterfallList()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.requestResult.observe(viewLifecycleOwner) { result ->
//            MobileApplication.showToast("RxJava Success: Loaded ${result.title} details!")
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) showLoading() else hideLoading()
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            error?.let {
                MobileApplication.showToast("RxJava Error: $it")
                viewModel.setError(null)
            }
        }
    }

    private fun setupLabels() {
        val labels = listOf(
            "Hot", "New", "Trending", "Summer Style", "Electronics",
            "Gift Ideas", "Photography", "Travel", "Home Decor", "Best Seller 2024"
        )

        binding.labelChipGroup.removeAllViews()
        labels.forEach { label ->
            val chip = Chip(context).apply {
                text = label
                isCheckable = true
            }
            binding.labelChipGroup.addView(chip)
        }
    }

    private fun setupWaterfallList() {
        val staggeredGridLayoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        // 1. 设置不自动调整间隙，配合 invalidateSpanAssignments 使用
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.waterfallRecyclerView.layoutManager = staggeredGridLayoutManager
        binding.waterfallRecyclerView.setItemAnimator(null) // 关闭动画，减少闪烁

        // 2. 监听滚动状态，当滑动停止时重新修正布局，防止顶部出现大空白
        binding.waterfallRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    staggeredGridLayoutManager.invalidateSpanAssignments()
                }
            }
        })

        // Generate dummy data with varying aspect ratios for the waterfall effect
        val dummyData = List(30) { i ->
            val w = 400
            val h = Random.nextInt(400, 801) // 稍微大一点的随机高度范围
            val imageUrl = "https://picsum.photos/$w/$h?random=$i"
            // 3. 将图片宽高数据传入 Item
            GenericItem(i, "Item $i", "Description for item $i", imageUrl, w, h)
        }
        binding.waterfallRecyclerView.adapter = WaterfallAdapter(dummyData) { item ->
            viewModel.loadItemDetails(item)
        }
    }
}
