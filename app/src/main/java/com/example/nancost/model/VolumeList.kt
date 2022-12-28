package com.example.nancost.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class VolumeList (
    @SerializedName("received_volume")
    var receivedVolume: Double? = null,

    @SerializedName("day_added")
    var dayAdded: String? = SimpleDateFormat("dd/MM/yyyy").format(Date())
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(receivedVolume)
        parcel.writeString(dayAdded)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<VolumeList> {
        override fun createFromParcel(parcel: Parcel): VolumeList {
            return VolumeList(parcel)
        }

        override fun newArray(size: Int): Array<VolumeList?> {
            return arrayOfNulls(size)
        }
    }
}