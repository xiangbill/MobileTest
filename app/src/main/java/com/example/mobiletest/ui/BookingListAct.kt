package com.example.mobiletest.ui

import android.content.Intent
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobiletest.adapter.BookingListAdapter
import com.example.mobiletest.adapter.BookingListClickCallBack
import com.example.mobiletest.base.BaseActivity
import com.example.mobiletest.databinding.ActivityBookingListBinding
import com.example.mobiletest.model.Booking
import com.example.mobiletest.model.BookingItem
import com.example.mobiletest.viewModel.BookingViewModel

class BookingListAct : BaseActivity<ActivityBookingListBinding>(ActivityBookingListBinding::inflate) {

    private lateinit var bookingListAdapter: BookingListAdapter
    private lateinit var viewModel: BookingViewModel
    private var bookingList = ArrayList<BookingItem>()
    private var isRefresh = false

    override fun initView() {
        viewModel = ViewModelProvider(this)[BookingViewModel::class.java]
        viewModel.initCache(this)
        bookingListAdapter = BookingListAdapter(bookingList)
        binding.recyclerViewBooking.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewBooking.adapter = bookingListAdapter
        bookingListAdapter.clickCallBack = object : BookingListClickCallBack {
            override fun orgClick(url: String) {
                intentUrl(url)
            }

            override fun destClick(url: String) {
                intentUrl(url)
            }
        }
    }

    override fun initData() {
        viewModel.getBookingList(this, isRefresh)
        viewModel.bookingList.observe(this) {
            if (it == null) {
                Toast.makeText(this, "Loading Fail", Toast.LENGTH_LONG).show()
            } else {
                bookingListAdapter.updateList(it.segments)
                bookingList = bookingListAdapter.getList()
                setViewValue(it)
            }
        }
        
        viewModel.isLoading.observe(this) { loading ->
            // Update loading UI if needed
        }
    }

    override fun onResume() {
        super.onResume()
        if (isRefresh) {
            Toast.makeText(this, "Refresh...", Toast.LENGTH_SHORT).show()
            viewModel.getBookingList(this, true)
            isRefresh = false
        }
    }

    override fun onPause() {
        super.onPause()
        isRefresh = true
    }

    private fun intentUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setViewValue(booking: Booking) {
        binding.apply {
            tvBookingListShipReference.text = booking.shipReference
            tvBookingListDuration.text = "${booking.duration}"
            tvBookingListExpiryTime.text = booking.expiryTime
            tvBookingListShipToken.text = booking.shipToken
        }
    }
}
