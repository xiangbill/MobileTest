package com.example.mobiletest.ui.waterfall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mobiletest.databinding.FragmentWaterfallBinding

import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletest.adapter.WaterfallAdapter
import com.google.android.material.chip.Chip
import com.example.mobiletest.model.GenericItem
import kotlin.random.Random

class WaterfallFragment : Fragment() {

    private var _binding: FragmentWaterfallBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWaterfallBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLabels()
        setupWaterfallList()
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
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
        binding.waterfallRecyclerView.layoutManager = staggeredGridLayoutManager
        binding.waterfallRecyclerView.setItemAnimator(null); // 关闭动画，减少闪烁/空白

        // Generate dummy data with varying aspect ratios for the waterfall effect
        val dummyData = List(30) { i ->
            val width = 400
            val height = Random.nextInt(300, 801) // Varying heights from 300 to 800
            val imageUrl = "https://picsum.photos/$width/$height?random=$i"
            GenericItem(i, "Item $i", "Description for item $i", imageUrl)
        }
        binding.waterfallRecyclerView.adapter = WaterfallAdapter(dummyData)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
