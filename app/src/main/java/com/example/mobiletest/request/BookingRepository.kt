package com.example.mobiletest.request

import android.content.Context
import android.util.Log
import com.example.mobiletest.model.Booking
import com.google.gson.Gson
import kotlinx.coroutines.delay
import utils.UtilsTools

class BookingRepository {
    suspend fun getBookingList(context: Context): Booking {
        delay(1000)
        val bookingStr = UtilsTools.parseJsonFromAssets2(context, "booking.json")
        Log.e("INFO", "booking data : $bookingStr")
        return Gson().fromJson(bookingStr, Booking::class.java)
    }
}