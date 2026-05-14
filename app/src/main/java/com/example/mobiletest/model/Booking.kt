package com.example.mobiletest.model

data class Booking(
    var shipReference: String,
    var shipToken: String,
    var canIssueTicketChecking: Boolean,
    var expiryTime: String,
    var duration: Int,
    var segments: ArrayList<BookingItem>
)
