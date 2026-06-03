package com.example.mobiletest.model

sealed class DetailItem {
    data class Header(val title: String) : DetailItem()
    data class Content(val text: String) : DetailItem()
    data class Image(val url: String) : DetailItem()
}
