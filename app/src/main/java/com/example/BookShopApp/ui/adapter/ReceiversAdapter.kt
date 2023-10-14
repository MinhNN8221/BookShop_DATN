package com.example.BookShopApp.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.databinding.ItemReceiverBinding

class ReceiversAdapter : RecyclerView.Adapter<ReceiversAdapter.ViewHolder>() {
    private var receiverList: MutableList<Receiver> = mutableListOf()
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiversAdapter.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemReceiverBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(receiver: List<Receiver>) {
        receiverList.clear()
        receiverList.addAll(receiver)
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: ReceiversAdapter.ViewHolder, position: Int) {
        holder.bind(receiverList[position])
    }

    override fun getItemCount(): Int {
        return receiverList.size
    }

    inner class ViewHolder(private val binding: ItemReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(receiver: Receiver) {
            binding.apply {
                Log.d("HELLO", receiver.receiverName)
                idReceiver.text = receiver.receiverId.toString()
                textReceiverName.text = receiver.receiverName
                textReceiverPhone.text = receiver.receiverPhone
                textReceiverAddress.text = receiver.receiverAddress
                if (receiver.isDefault == 1) {
                    textDefault.visibility = View.VISIBLE
                }
                textUpdate.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClickListener?.onItemClick(position)
                    }
                }
            }
        }
    }

}