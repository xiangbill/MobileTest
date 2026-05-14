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
import retrofit2.http.Url

class BookingListAct : AppCompatActivity() {

    private lateinit var bookingListAdapter: BookingListAdapter
    private lateinit var viewModel: BookingViewModel
    private var bookingList = mutableListOf<BookingItem>()
    private lateinit var booking: Booking
    private lateinit var dataBinding: ActivityBookingListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityBookingListBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        viewModel = ViewModelProvider(this)[BookingViewModel::class.java]
        viewModel.initCache(this)

    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "Refresh...", Toast.LENGTH_SHORT).show()
        init()
    }

    private fun intentUrl(url: String) {
       try{
           val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
           startActivity(intent)
       }catch(e : Exception){
           e.printStackTrace()
       }
    }

    private fun init() {
        viewModel.getBookingList(this)
        viewModel.bookingList.observe(this) {
            if (it == null) {
                Toast.makeText(this, "Loading Fail", Toast.LENGTH_LONG).show()
            } else {
                bookingListAdapter = BookingListAdapter(it.segments)
                bookingListAdapter.clickCallBack = object : BookingListClickCallBack {
                    override fun orgClick(url: String) {
                        intentUrl(url)
                    }

                    override fun destClick(url: String) {
                        intentUrl(url)
                    }
                }
                dataBinding.recyclerViewBooking.layoutManager = LinearLayoutManager(this)
                dataBinding.recyclerViewBooking.adapter = bookingListAdapter
            }
        }
    }
}