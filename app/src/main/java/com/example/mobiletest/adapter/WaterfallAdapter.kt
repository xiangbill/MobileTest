package com.example.mobiletest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mobiletest.base.BaseAdapter
import com.example.mobiletest.databinding.ItemWaterfallBinding
import com.example.mobiletest.model.GenericItem
import java.util.Random

class WaterfallAdapter(
    items: List<GenericItem>,
    private val onItemClick: (GenericItem) -> Unit = {}
) : BaseAdapter<GenericItem, ItemWaterfallBinding>(items.toMutableList()) {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemWaterfallBinding {
        return ItemWaterfallBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemWaterfallBinding, item: GenericItem, position: Int) {
        binding.waterfallTitle.text = item.title

        binding.root.setOnClickListener {
            onItemClick(item)
        }

        // --- 核心优化：预设 ImageView 高度 (兼容无宽高数据情况) ---
        val layoutParams = binding.waterfallImage.layoutParams
        val screenWidth = binding.root.context.resources.displayMetrics.widthPixels
        val itemWidth = screenWidth / 2 // 2 列瀑布流下的单项宽度

        if (item.width > 0 && item.height > 0) {
            // A. 如果后端返回了宽高：使用真实比例计算高度 (完美适配)
            layoutParams.height = (item.height.toFloat() / item.width.toFloat() * itemWidth).toInt()
        } else {
            // B. 兼容方案：如果后端没返回宽高
            val random = Random(item.id.toLong())
            val ratio = 0.8f + random.nextFloat() * 0.7f // 随机产生 0.8 到 1.5 之间的宽高比
            layoutParams.height = (itemWidth * ratio).toInt()
        }
        
        binding.waterfallImage.layoutParams = layoutParams

        Glide.with(binding.waterfallImage.context)
            .load(item.imageUrl)
            .centerCrop() // 配合随机高度使用，确保图片填满占位区域
            .into(binding.waterfallImage)
    }
}
