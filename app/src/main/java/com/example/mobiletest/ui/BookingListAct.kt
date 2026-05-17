package com.example.mobiletest.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobiletest.adapter.BookingListAdapter
import com.example.mobiletest.adapter.BookingListClickCallBack
import com.example.mobiletest.databinding.ActivityBookingListBinding
import com.example.mobiletest.model.Booking
import com.example.mobiletest.model.BookingItem
import com.example.mobiletest.viewModel.BookingViewModel

class BookingListAct : AppCompatActivity() {

    private lateinit var bookingListAdapter: BookingListAdapter
    private lateinit var viewModel: BookingViewModel
    private var bookingList = ArrayList<BookingItem>()
    private lateinit var dataBinding: ActivityBookingListBinding
    private var isRefresh = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityBookingListBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        init()
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
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun init() {
        viewModel = ViewModelProvider(this)[BookingViewModel::class.java]
        viewModel.initCache(this)
        bookingListAdapter = BookingListAdapter(bookingList)
        dataBinding.recyclerViewBooking.layoutManager = LinearLayoutManager(this)
        dataBinding.recyclerViewBooking.adapter = bookingListAdapter
        bookingListAdapter.clickCallBack = object : BookingListClickCallBack {
            override fun orgClick(url: String) {
                intentUrl(url)
            }

            override fun destClick(url: String) {
                intentUrl(url)
            }
        }
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
    }

    private fun setViewValue(booking: Booking) {
        booking.let {
            dataBinding.tvBookingListShipReference.text = it.shipReference
            dataBinding.tvBookingListDuration.text = "${it.duration}"
            dataBinding.tvBookingListExpiryTime.text = it.expiryTime
            dataBinding.tvBookingListShipToken.text = it.shipToken
        }
    }
}