package com.example.nancost.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Nancost(
    @SerializedName("nancost_id")
    var nancostUid: String? = null,

    @SerializedName("nancost_name")
    var nancostName: String? = null,

    @SerializedName("remain_volume")
    var remainVolume: Double? = null,

    @SerializedName("received_volume")
    var receivedVolume: Double? = null,

    @SerializedName("nancost_data")
    var nancostDataList: ArrayList<NancostData?>? = arrayListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.createTypedArrayList(NancostData)
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nancostUid)
        parcel.writeString(nancostName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Nancost> {
        override fun createFromParcel(parcel: Parcel): Nancost {
            return Nancost(parcel)
        }

        override fun newArray(size: Int): Array<Nancost?> {
            return arrayOfNulls(size)
        }
    }
}