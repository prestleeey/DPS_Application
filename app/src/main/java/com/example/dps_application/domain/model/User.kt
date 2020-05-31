package com.example.dps_application.domain.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName
import org.json.JSONObject

@Entity(
    tableName = "users"
)
data class User(
    @PrimaryKey
    @SerializedName("id")
    @ColumnInfo(name = "id")
    var id: Int = 0,
    @SerializedName("vk_id")
    @ColumnInfo(name = "vk_id")
    var vkId: Int = 0,
    @SerializedName("first_name")
    @ColumnInfo(name = "first_name")
    var firstName: String = "",
    @SerializedName("last_name")
    @ColumnInfo(name = "last_name")
    var lastName: String = "",
    @SerializedName("photo_200")
    @ColumnInfo(name = "photo_200")
    var photo: String = "",
    @Ignore
    var deactivated: Boolean = false) : Parcelable {

    // for vk sdk
    constructor(parcel: Parcel) : this(
        id = 0,
        vkId = parcel.readInt(),
        firstName = parcel.readString() ?: "",
        lastName = parcel.readString() ?: "",
        photo = parcel.readString() ?: "",
        deactivated = parcel.readByte() != 0.toByte())

    // for vk sdk
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(vkId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(photo)
        parcel.writeByte(if (deactivated) 1 else 0)
    }

    // for vk sdk
    override fun describeContents(): Int {
        return 0
    }

    // for vk sdk
    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }

        fun parse(json: JSONObject)
                = User(
            vkId = json.optInt("id", 0),
            firstName = json.optString("first_name", ""),
            lastName = json.optString("last_name", ""),
            photo = json.optString("photo_200", ""),
            deactivated = json.optBoolean("deactivated", false)
        )
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(other?.javaClass != javaClass) return false

        other as User

        return this.id == other.id
    }

}