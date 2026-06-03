package com.example.mobiletest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.mobiletest.databinding.ItemDetailContentBinding
import com.example.mobiletest.databinding.ItemDetailHeaderBinding
import com.example.mobiletest.databinding.ItemDetailImageBinding
import com.example.mobiletest.model.DetailItem

class DetailAdapter(private val items: List<DetailItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_CONTENT = 1
        private const val TYPE_IMAGE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DetailItem.Header -> TYPE_HEADER
            is DetailItem.Content -> TYPE_CONTENT
            is DetailItem.Image -> TYPE_IMAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_HEADER -> HeaderViewHolder(ItemDetailHeaderBinding.inflate(inflater, parent, false))
            TYPE_CONTENT -> ContentViewHolder(ItemDetailContentBinding.inflate(inflater, parent, false))
            TYPE_IMAGE -> ImageViewHolder(ItemDetailImageBinding.inflate(inflater, parent, false))
            else -> throw IllegalArgumentException("Unknown viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is DetailItem.Header -> (holder as HeaderViewHolder).bind(item)
            is DetailItem.Content -> (holder as ContentViewHolder).bind(item)
            is DetailItem.Image -> (holder as ImageViewHolder).bind(item)
        }
    }

    override fun getItemCount(): Int = items.size

    class HeaderViewHolder(private val binding: ItemDetailHeaderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailItem.Header) {
            binding.tvHeader.text = item.title
        }
    }

    class ContentViewHolder(private val binding: ItemDetailContentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailItem.Content) {
            binding.tvContent.text = item.text
        }
    }

    class ImageViewHolder(private val binding: ItemDetailImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailItem.Image) {
            Glide.with(binding.ivDetail.context)
                .load(item.url)
                .into(binding.ivDetail)
        }
    }
}
