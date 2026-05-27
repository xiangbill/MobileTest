package com.example.mobiletest.model

data class GenericItem(
    val id: Int,
    val title: String,
    val description: String,
    val imageUrl: String = "https://picsum.photos/200/300?random=$id",
    val width: Int = 0,
    val height: Int = 0
)
