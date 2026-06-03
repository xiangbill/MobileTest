package com.example.mobiletest.viewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mobiletest.base.BaseViewModel
import com.example.mobiletest.manager.BookingCacheManager
import com.example.mobiletest.model.Booking
import com.example.mobiletest.model.BookingItem
import com.example.mobiletest.request.BookingRepository
import kotlinx.coroutines.launch

class BookingViewModel : BaseViewModel() {

    private val repo = BookingRepository()
    private lateinit var cacheManager: BookingCacheManager
    var bookingList = MutableLiveData<Booking?>()

    fun initCache(context: Context) {
        cacheManager = BookingCacheManager(context)
    }

    fun getBookingList(context: Context, isRefresh : Boolean) {
        viewModelScope.launch {
            setLoading(true)
            try {
                if (cacheManager.isCacheValid()) {
                    val booking = cacheManager.getBooking()
                    bookingList.postValue(booking)
                } else {
                    val response = repo.getBookingList(context,isRefresh)
                    bookingList.postValue(response)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                setError(e.message)
                bookingList.postValue(null)
            } finally {
                setLoading(false)
            }
        }
    }

    fun deleteBookingItem(booking: Booking, item: BookingItem) {
        viewModelScope.launch {
            booking.segments.remove(item)
        }
    }
}
