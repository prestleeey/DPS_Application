package com.example.dps_application.ui.chat

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dps_application.R
import com.example.dps_application.domain.model.response.MessageResponse
import com.example.dps_application.domain.repository.UserRepository
import com.example.dps_application.ui.chat.view.ReceivedMessageHolder
import com.example.dps_application.ui.chat.view.SentMessageHolder
import javax.inject.Inject

class MessageAdapter
@Inject constructor(
    private val userRepository: UserRepository
) : PagedListAdapter<MessageResponse, RecyclerView.ViewHolder>(MESSAGE_COMPARATOR) {

    override fun getItemViewType(position: Int): Int {
        return if(getItem(position)?.user?.id == userRepository.getUserInfoFromBase().id) {
            R.layout.message_sent
        } else {
            R.layout.message_received
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            R.layout.message_received -> ReceivedMessageHolder.create(parent)
            R.layout.message_sent -> SentMessageHolder.create(parent)
            else -> throw IllegalArgumentException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //Log.d("1337", "Visible item ${(getItem(position) as MessageJSON).messageId} Last Visible item ${(getItem(if (position - 8 > 0) position - 8 else position) as MessageJSON).messageId}");
        when (getItemViewType(position)) {
            R.layout.message_received -> (holder as ReceivedMessageHolder).bind(getItem(position))
            R.layout.message_sent -> (holder as SentMessageHolder).bind(getItem(position))
        }
    }

    companion object {
        val MESSAGE_COMPARATOR = object : DiffUtil.ItemCallback<MessageResponse>() {
            override fun areItemsTheSame(oldItem: MessageResponse, newItem: MessageResponse): Boolean
                    = oldItem.messageId == newItem.messageId && oldItem.chatId == newItem.chatId


            override fun areContentsTheSame(oldItem: MessageResponse, newItem: MessageResponse): Boolean
                    = oldItem.text == newItem.text && oldItem.date == newItem.date && oldItem.attachments.size == newItem.attachments.size

        }
    }

}