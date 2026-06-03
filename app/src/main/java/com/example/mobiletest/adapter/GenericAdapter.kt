package com.example.mobiletest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.mobiletest.base.BaseAdapter
import com.example.mobiletest.databinding.ItemGenericBinding
import com.example.mobiletest.model.GenericItem

class GenericAdapter(items: MutableList<GenericItem>, private val onItemClick: (GenericItem) -> Unit) :
    BaseAdapter<GenericItem, ItemGenericBinding>(items) {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ItemGenericBinding {
        return ItemGenericBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ItemGenericBinding, item: GenericItem, position: Int) {
        binding.itemTitle.text = item.title
        binding.itemDescription.text = item.description
        Glide.with(binding.itemImage.context)
            .load(item.imageUrl)
            .into(binding.itemImage)
        
        binding.root.setOnClickListener {
            onItemClick(item)
        }
    }
}
