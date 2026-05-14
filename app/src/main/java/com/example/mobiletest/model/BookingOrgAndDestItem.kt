package com.example.mobiletest.model

data class BookingOrgAndDestItem(
    var destination: BookingDestinationItem,
    var destinationCity: String,
    var origin: BookingOriginItem,
    var originCity: String,
)
