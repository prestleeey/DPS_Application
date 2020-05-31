package com.example.dps_application.ui.chat.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.dps_application.R
import com.example.dps_application.domain.model.response.AttachmentResponse
import com.example.dps_application.util.AttachmentTypes
import com.example.dps_application.util.px
import com.makeramen.roundedimageview.RoundedImageView
import com.makeramen.roundedimageview.RoundedTransformationBuilder
import com.squareup.picasso.Picasso
import com.squareup.picasso.Transformation


class AttachmentsContainerView(
    context: Context,
    attrs: AttributeSet
) : LinearLayoutCompat(context, attrs) {

    private var attachments: List<AttachmentResponse> = listOf()

    fun setAttachments(attachments: List<AttachmentResponse>) {
        this.attachments = attachments

        if(childCount > 1)
            removeViews(1, childCount - 1)

        val lastIndex = attachments.lastIndex
        attachments.forEachIndexed { index, attachmentJSON ->
            when(attachmentJSON.type) {
                AttachmentTypes.IMAGE.name -> {
                    createImageView(attachmentJSON.url, lastIndex == index)
                }
            }
        }
    }

    private fun createImageView(url: String, isLastItem: Boolean) {
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        params.gravity = Gravity.CENTER
        if(!isLastItem)
            params.bottomMargin = 3.px

        val image = RoundedImageView(context)
        image.layoutParams = params
        image.background = context.getDrawable(R.drawable.rounded_imageview_background)
        image.adjustViewBounds = true
        image.maxWidth = 240.px

        addView(image)

        val transformation: Transformation = RoundedTransformationBuilder()
            .cornerRadiusDp(10f)
            .build()

        Picasso.get()
            .load(url)
            .transform(transformation)
            .into(image)

    }

}