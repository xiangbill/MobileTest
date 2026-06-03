package com.example.mobiletest.ui.detail

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobiletest.adapter.DetailAdapter
import com.example.mobiletest.base.BaseFragment
import com.example.mobiletest.databinding.FragmentDetailBinding
import com.example.mobiletest.model.DetailItem

class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {

    override fun initView() {
        val title = arguments?.getString("title") ?: "Detail Page"
        val description = arguments?.getString("description") ?: "No description available."
        val imageUrl = arguments?.getString("imageUrl") ?: ""

        val items = listOf(
            DetailItem.Header(title),
            DetailItem.Image(imageUrl),
            DetailItem.Content(description),
            DetailItem.Content("More detailed information about this item can be found here. This is a multi-layout RecyclerView demonstration."),
            DetailItem.Image("https://picsum.photos/id/1/800/400"),
            DetailItem.Content("Another paragraph to show scrollability and layout variety.")
        )

        binding.rvDetail.layoutManager = LinearLayoutManager(context)
        binding.rvDetail.adapter = DetailAdapter(items)
    }
}
