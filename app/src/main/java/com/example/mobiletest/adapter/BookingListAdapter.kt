package com.example.mobiletest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobiletest.databinding.ActivityBookingItemBinding
import com.example.mobiletest.model.BookingItem


class BookingListAdapter(private val bookingList: ArrayList<BookingItem>) :
    RecyclerView.Adapter<BookingListAdapter.BookingViewHolder>() {

    var clickCallBack: BookingListClickCallBack? = null

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val bookingItem = bookingList[position]
        holder.initItem(bookingItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val itemBinding =
            ActivityBookingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    inner class BookingViewHolder(private val bookingItemBinding: ActivityBookingItemBinding) :
        RecyclerView.ViewHolder(bookingItemBinding.root) {
        fun initItem(item: BookingItem) {
            item.apply {
                bookingItemBinding.tvBookingListItemDestCity.text =
                    this.originAndDestinationPair.destinationCity
                bookingItemBinding.tvBookingListItemOrgCity.text =
                    this.originAndDestinationPair.originCity
                bookingItemBinding.tvBookingListItemDestName.text =
                    this.originAndDestinationPair.destination.displayName
                bookingItemBinding.tvBookingListItemOrgName.text =
                    this.originAndDestinationPair.origin.displayName
                bookingItemBinding.btnBookingListItemDestUrl.setOnClickListener {
                    clickCallBack?.destClick(this.originAndDestinationPair.destination.url)
                }
                bookingItemBinding.btnBookingListItemOrgUrl.setOnClickListener {
                    clickCallBack?.orgClick(this.originAndDestinationPair.origin.url)
                }
            }
        }
    }

    fun removeItem(item: BookingItem, index: Int) {
        bookingList.remove(item)
        notifyItemRemoved(index)
    }

    fun getList(): ArrayList<BookingItem> {
        return bookingList
    }

    fun updateList(newBookingList: ArrayList<BookingItem>) {
        bookingList.clear()
        bookingList.addAll(newBookingList)
        notifyDataSetChanged()
    }
}