package com.example.dps_application.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.example.dps_application.R
import com.example.dps_application.databinding.FragmentChatBinding
import com.example.dps_application.di.Injectable
import kotlinx.android.synthetic.main.fragment_chat.*
import javax.inject.Inject


class ChatFragment : Fragment(), Injectable {

    @Inject
    lateinit var adapter: MessageAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val chatViewModel: ChatViewModel by viewModels {
        viewModelFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChatBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_chat, container, false)

        binding.chatViewModel = chatViewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initAdapter()

//        FirebaseInstanceId.getInstance().instanceId
//            .addOnCompleteListener(OnCompleteListener { task ->
//                if (!task.isSuccessful) {
//                    Log.w("1337", "getInstanceId failed", task.exception)
//                    return@OnCompleteListener
//                }
//
//                // Get new Instance ID token
//                val token = task.result?.token
//
//                // Log and toast
//                Log.d("1337", "token $token")
//            })
//
//        FirebaseMessaging.getInstance().subscribeToTopic("1")
//            .addOnCompleteListener { task ->
//                var msg = "Failure"
//                if (!task.isSuccessful) {
//                    msg = "Successful"
//                }
//                Log.d("1337", msg)
//            }

    }

    override fun onResume() {
        super.onResume()
        chatViewModel.refresh()
    }

    private fun initAdapter() {
        list.adapter = adapter

        adapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if(positionStart == 0) {
                    list.layoutManager?.scrollToPosition(0)
                }
            }
        })

        chatViewModel.messages.observe(viewLifecycleOwner, adapter::submitList)

        chatViewModel.sendMessageEvent().observe(viewLifecycleOwner) {
            edit_text_chat_box.setText("")
        }
    }
}