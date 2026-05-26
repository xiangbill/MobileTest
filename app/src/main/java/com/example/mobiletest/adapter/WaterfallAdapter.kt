package com.example.mobiletest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobiletest.databinding.ItemWaterfallBinding
import com.example.mobiletest.model.GenericItem

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
        
        Glide.with(holder.binding.waterfallImage.context)
            .load(item.imageUrl)
            .into(holder.binding.waterfallImage)
    }

    override fun getItemCount() = items.size
}
