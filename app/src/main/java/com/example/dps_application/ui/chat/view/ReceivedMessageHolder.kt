package com.example.dps_application.ui.chat.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dps_application.R
import com.example.dps_application.databinding.MessageReceivedBinding
import com.example.dps_application.domain.model.response.MessageResponse

class ReceivedMessageHolder(private val binding: MessageReceivedBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: MessageResponse?) {
        binding.message = message ?: MessageResponse()
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): ReceivedMessageHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding: MessageReceivedBinding =
                DataBindingUtil.inflate(inflater, R.layout.message_received, parent, false)
            return ReceivedMessageHolder(
                binding
            )
        }
    }
}