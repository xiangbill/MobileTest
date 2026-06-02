package com.example.mobiletest.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mobiletest.databinding.ItemGenericBinding
import com.example.mobiletest.model.GenericItem

class GenericAdapter(private val items: MutableList<GenericItem>) :
    RecyclerView.Adapter<GenericAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemGenericBinding) : RecyclerView.ViewHolder(binding.root)

    fun addData(newItems: List<GenericItem>) {
        val startPos = items.size
        items.addAll(newItems)
        notifyItemRangeInserted(startPos, newItems.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newItems: List<GenericItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGenericBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.itemTitle.text = item.title
        holder.binding.itemDescription.text = item.description
        Glide.with(holder.binding.itemImage.context)
            .load(item.imageUrl)
            .into(holder.binding.itemImage)
    }

    override fun getItemCount() = items.size
}
