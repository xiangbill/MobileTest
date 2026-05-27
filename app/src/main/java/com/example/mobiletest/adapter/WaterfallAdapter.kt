package com.example.mobiletest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobiletest.databinding.ItemWaterfallBinding
import com.example.mobiletest.model.GenericItem
import java.util.Random

class WaterfallAdapter(private val items: List<GenericItem>) :
    RecyclerView.Adapter<WaterfallAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemWaterfallBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemWaterfallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.waterfallTitle.text = item.title

        // --- 核心优化：预设 ImageView 高度 (兼容无宽高数据情况) ---
        val layoutParams = holder.binding.waterfallImage.layoutParams
        val screenWidth = holder.itemView.context.resources.displayMetrics.widthPixels
        val itemWidth = screenWidth / 2 // 2 列瀑布流下的单项宽度

        if (item.width > 0 && item.height > 0) {
            // A. 如果后端返回了宽高：使用真实比例计算高度 (完美适配)
            layoutParams.height = (item.height.toFloat() / item.width.toFloat() * itemWidth).toInt()
        } else {
            // B. 兼容方案：如果后端没返回宽高
            // 使用 item.id 作为随机种子，确保同一个 item 无论滑动多少次，算出来的比例都【永远一致】。
            // 只要高度不再跳动，StaggeredGridLayout 就不会产生空白间隙。
            val random = Random(item.id.toLong())
            val ratio = 0.8f + random.nextFloat() * 0.7f // 随机产生 0.8 到 1.5 之间的宽高比
            layoutParams.height = (itemWidth * ratio).toInt()
        }
        
        holder.binding.waterfallImage.layoutParams = layoutParams

        Glide.with(holder.binding.waterfallImage.context)
            .load(item.imageUrl)
            .centerCrop() // 配合随机高度使用，确保图片填满占位区域
            .into(holder.binding.waterfallImage)
    }

    override fun getItemCount() = items.size
}
