package com.example.mobiletest.ui.detail

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobiletest.adapter.DetailAdapter
import com.example.mobiletest.base.BaseActivity
import com.example.mobiletest.databinding.ActivityDetailBinding
import com.example.mobiletest.model.DetailItem

class DetailActivity : BaseActivity<ActivityDetailBinding>(ActivityDetailBinding::inflate) {

    override fun initView() {
        val title = intent.getStringExtra("title") ?: "Detail Activity"
        val description = intent.getStringExtra("description") ?: "No description available."
        val imageUrl = intent.getStringExtra("imageUrl") ?: ""

        val items = listOf(
            DetailItem.Header("Activity Multi-Layout"),
            DetailItem.Header(title),
            DetailItem.Image(imageUrl),
            DetailItem.Content(description),
            DetailItem.Image("https://picsum.photos/id/101/800/400"),
            DetailItem.Content("This page is an Activity, not a Fragment. It demonstrates multi-layout RecyclerView by mixing headers, images, and text content."),
            DetailItem.Image("https://picsum.photos/id/102/800/400"),
            DetailItem.Content("End of Activity Detail Content.")
        )

        binding.rvDetail.layoutManager = LinearLayoutManager(this)
        binding.rvDetail.adapter = DetailAdapter(items)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail Activity"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
