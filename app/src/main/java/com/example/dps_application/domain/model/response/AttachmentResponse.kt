package com.example.dps_application.domain.model.response

import androidx.room.ColumnInfo
import com.example.dps_application.util.AttachmentTypes
import com.google.gson.annotations.SerializedName

class AttachmentResponse {
    @SerializedName("type")
    @ColumnInfo(name = "type")
    var type: String = AttachmentTypes.UNKNOWN.name
    @SerializedName("url")
    @ColumnInfo(name = "url")
    var url: String = ""

    fun isEmpty() = type == AttachmentTypes.UNKNOWN.name && url == ""
}