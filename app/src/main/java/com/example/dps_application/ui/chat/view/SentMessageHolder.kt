package com.example.dps_application.ui.chat.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dps_application.R
import com.example.dps_application.databinding.MessageSentBinding
import com.example.dps_application.domain.model.response.MessageResponse


class SentMessageHolder(private val binding: MessageSentBinding)
    : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: MessageResponse?) {
        binding.message = message ?: MessageResponse()
        binding.executePendingBindings()
    }

    companion object {
        fun create(parent: ViewGroup): SentMessageHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding: MessageSentBinding =
                DataBindingUtil.inflate(inflater, R.layout.message_sent, parent, false)
            return SentMessageHolder(
                binding
            )
        }
    }
}