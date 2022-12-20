package com.example.nancost.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class NancostData(
    @SerializedName("nancost_data_id")
    var nancostDataUid: String? = null,

    @SerializedName("received_volume")
    var receivedVolume: Double? = null,

    @SerializedName("delivered_leaves")
    var deliveredLeaves: Int? = null,

    @SerializedName("delivered_volume")
    var deliveredVolume: Double? = null,

    @SerializedName("remain_volume")
    var remainVolume: Double? = null,

    @SerializedName("unit_price")
    var unitPrice: Double = 8000.0,

    @SerializedName("amount_will_pay")
    var amountWillPay: Double? = null,

    @SuppressLint("SimpleDateFormat") @SerializedName("day_added")
    var dayAdded: String? = SimpleDateFormat("dd/MM/yyyy").format(Date())
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readDouble(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString()
    ) {
    }

    @JvmName("remainVolume")
    fun getRemainingVolume() = deliveredVolume?.let { receivedVolume?.minus(it) }

    @JvmName("amountWillPay")
    fun getAmountPay() = deliveredLeaves?.times(unitPrice)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nancostDataUid)
        parcel.writeValue(receivedVolume)
        parcel.writeValue(deliveredLeaves)
        parcel.writeValue(deliveredVolume)
        parcel.writeValue(remainVolume)
        parcel.writeDouble(unitPrice)
        parcel.writeValue(amountWillPay)
        parcel.writeString(dayAdded)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NancostData> {
        override fun createFromParcel(parcel: Parcel): NancostData {
            return NancostData(parcel)
        }

        override fun newArray(size: Int): Array<NancostData?> {
            return arrayOfNulls(size)
        }
    }
}