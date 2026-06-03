package com.example.mobiletest.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.mobiletest.base.BaseAdapter
import com.example.mobiletest.databinding.ActivityBookingItemBinding
import com.example.mobiletest.model.BookingItem

class BookingListAdapter(bookingList: ArrayList<BookingItem>) :
    BaseAdapter<BookingItem, ActivityBookingItemBinding>(bookingList) {

    var clickCallBack: BookingListClickCallBack? = null

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup): ActivityBookingItemBinding {
        return ActivityBookingItemBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: ActivityBookingItemBinding, item: BookingItem, position: Int) {
        binding.tvBookingListItemDestCity.text = item.originAndDestinationPair.destinationCity
        binding.tvBookingListItemOrgCity.text = item.originAndDestinationPair.originCity
        binding.tvBookingListItemDestName.text = item.originAndDestinationPair.destination.displayName
        binding.tvBookingListItemOrgName.text = item.originAndDestinationPair.origin.displayName
        
        binding.btnBookingListItemDestUrl.setOnClickListener {
            clickCallBack?.destClick(item.originAndDestinationPair.destination.url)
        }
        binding.btnBookingListItemOrgUrl.setOnClickListener {
            clickCallBack?.orgClick(item.originAndDestinationPair.origin.url)
        }
    }

    fun removeItem(item: BookingItem, index: Int) {
        items.remove(item)
        notifyItemRemoved(index)
    }

    fun updateList(newBookingList: ArrayList<BookingItem>) {
        setData(newBookingList)
    }
}
