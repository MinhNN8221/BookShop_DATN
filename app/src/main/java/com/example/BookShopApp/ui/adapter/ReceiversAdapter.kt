package com.example.BookShopApp.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.databinding.ItemBookBinding
import com.example.BookShopApp.databinding.ItemFooterReceiverBinding
import com.example.BookShopApp.databinding.ItemReceiverBinding

class ReceiversAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var receiverList: MutableList<Receiver> = mutableListOf()
    private var onItemRemoveListener: OnItemClickListener? = null
    private var onItemUpdateListener: OnItemClickListener? = null
    private var onItemSelectedListener: OnItemClickListener? = null
    private var onItemAddListener: OnItemClickListener? = null

    companion object {
        private const val VIEW_TYPE_RECEIVER = 0
        private const val VIEW_TYPE_FOOTER = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_RECEIVER) {
            val binding = ItemReceiverBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
        } else {
            val binding = ItemFooterReceiverBinding.inflate(inflater, parent, false)
            ViewHolderFooter(binding)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(receiver: List<Receiver>) {
        receiverList.clear()
        receiverList.addAll(receiver)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < receiverList.size) VIEW_TYPE_RECEIVER else VIEW_TYPE_FOOTER
    }

    fun setOnItemRemoveListener(listener: OnItemClickListener) {
        onItemRemoveListener = listener
    }

    fun setOnItemSelectedListener(listener: OnItemClickListener) {
        onItemSelectedListener = listener
    }

    fun setOnItemUpdateistener(listener: OnItemClickListener) {
        onItemUpdateListener = listener
    }
    fun setOnItemAddListener(listener: OnItemClickListener) {
        onItemAddListener = listener
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is ViewHolder -> holder.bind(receiverList[position])
            is ViewHolderFooter -> holder.bindFooter()
        }
    }

    fun getReceiver(position: Int): Receiver {
        return receiverList[position]
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeData(position: Int) {
        receiverList.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setIsSelected() {
        receiverList[0].isSelected = 1
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeIsSelected(position: Int){
        for(receiver in receiverList){
            if(receiver.isSelected==1 && receiverList[position]!=receiver)
                receiver.isSelected=0
        }
        receiverList[position].isSelected=1
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return receiverList.size + 1
    }

    inner class ViewHolderFooter(private val binding: ItemFooterReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindFooter() {
            binding?.apply {
                linearReceiverAddress.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemAddListener?.onItemClick(position)
                    }
                }
            }
        }
    }

    inner class ViewHolder(private val binding: ItemReceiverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(receiver: Receiver) {
            binding.apply {
                idReceiver.text = receiver.receiverId.toString()
                textReceiverName.text = receiver.receiverName
                textReceiverPhone.text = receiver.receiverPhone
                textReceiverAddress.text = receiver.receiverAddress
                if (receiver.isDefault == 1) {
                    textDefault.visibility = View.VISIBLE
                } else {
                    textDefault.visibility = View.GONE
                }
                radioButton.isChecked = receiver.isSelected == 1
                textRemove.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemRemoveListener?.onItemClick(position)
                    }
                }
                contraintReceiver.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemUpdateListener?.onItemClick(position)
                    }
                }
                radioButton.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onItemSelectedListener?.onItemClick(position)
                    }
                }
            }
        }
    }

}