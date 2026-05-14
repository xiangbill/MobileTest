package com.example.mobiletest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletest.R
import com.example.mobiletest.model.BookingItem


class BookingListAdapter(private val bookingList: MutableList<BookingItem>) :
    RecyclerView.Adapter<BookingListAdapter.BookingViewHolder>() {

    var clickCallBack: BookingListClickCallBack? = null

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val bookingItem = bookingList[position]
        holder.tvDestCity.text = bookingItem.originAndDestinationPair.destinationCity
        holder.tvOrgCity.text = bookingItem.originAndDestinationPair.originCity
        holder.tvDestName.text = bookingItem.originAndDestinationPair.destination.displayName
        holder.tvOrgName.text = bookingItem.originAndDestinationPair.origin.displayName

        holder.btnDestUrl.setOnClickListener {
            clickCallBack?.destClick(bookingItem.originAndDestinationPair.destination.url)
        }
        holder.btnOrgUrl.setOnClickListener {
            clickCallBack?.orgClick(bookingItem.originAndDestinationPair.origin.url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_booking_item, parent, false)
        return BookingViewHolder(view)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    inner class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDestCity: TextView = itemView.findViewById(R.id.tv_booking_list_item_destCity)
        val btnDestUrl: Button = itemView.findViewById(R.id.btn_booking_list_item_destUrl)
        val tvDestName: TextView = itemView.findViewById(R.id.tv_booking_list_item_destName)

        val tvOrgCity: TextView = itemView.findViewById(R.id.tv_booking_list_item_orgCity)
        val btnOrgUrl: TextView = itemView.findViewById(R.id.btn_booking_list_item_orgUrl)
        val tvOrgName: TextView = itemView.findViewById(R.id.tv_booking_list_item_orgName)
    }
}