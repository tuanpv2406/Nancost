package com.example.nancost.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

data class NancostVolume (
    @SerializedName("nancost_uid")
    var nancostUid: String? = null,

    @SerializedName("volume_list")
    var volumeList: ArrayList<VolumeList?>? = arrayListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nancostUid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NancostVolume> {
        override fun createFromParcel(parcel: Parcel): NancostVolume {
            return NancostVolume(parcel)
        }

        override fun newArray(size: Int): Array<NancostVolume?> {
            return arrayOfNulls(size)
        }
    }
}