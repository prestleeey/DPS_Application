package com.example.dps_application.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.example.dps_application.domain.model.response.AttachmentResponse
import com.example.dps_application.ui.chat.view.AttachmentsContainerView
import java.text.SimpleDateFormat
import java.util.*

class BindingAdapters {
    companion object {
        @BindingAdapter("app:url", "app:errorImage")
        @JvmStatic
        fun loadImage(view: ImageView, url: String, errorImage: Drawable) {
            Picasso.get().load(url).error(errorImage).into(view)
        }

        @BindingAdapter("app:date")
        @JvmStatic
        fun formatDate(view: TextView, date: Date) {
            val format = SimpleDateFormat("HH:mm")
            view.text = format.format(date)
        }

        @BindingAdapter("app:attachments")
        @JvmStatic
        fun setAttachments(view: AttachmentsContainerView, attachments: List<AttachmentResponse>) {
            view.setAttachments(attachments)
        }

    }
}