package com.example.mobiletest.manager

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.mobiletest.model.Booking
import com.google.gson.Gson

class BookingCacheManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("booking_cache", Context.MODE_PRIVATE)
    private val expireTime = 30 * 60 * 1000L
    private val bookingData = "booking_data"
    private val bookingDataTime = "booking_data_time"
    private val gson = Gson()
    fun saveBooking(booking: Booking) {
        sharedPreferences
            .edit()
            .putString(bookingData, gson.toJson(booking))
            .putLong(bookingDataTime, System.currentTimeMillis())
            .apply()
    }

    fun getBooking(): Booking {
        val bookingStr = sharedPreferences.getString(bookingData, "")
        Log.e("INFO", "booking data : $bookingStr")
        return gson.fromJson(bookingStr, Booking::class.java)
    }

    fun isCacheValid(): Boolean {
        val time = sharedPreferences.getLong(bookingDataTime, 0)
        return System.currentTimeMillis() - time < expireTime
    }

}