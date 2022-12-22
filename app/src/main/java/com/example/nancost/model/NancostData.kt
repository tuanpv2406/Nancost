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

    @SerializedName("nancost_id")
    var nancostUid: String? = null,

    @SerializedName("received_volume")
    var receivedVolume: Double? = null,

    @SerializedName("delivered_leaves")
    var deliveredLeaves: Int? = null,

    @SerializedName("delivered_volume")
    var deliveredVolume: Double? = null,

    @SerializedName("remain_volume")
    var remainVolume: Double? = null,

    @SerializedName("unit_price")
    var unitPrice: Int? = 8000,

    @SerializedName("amount_will_pay")
    var amountWillPay: Int? = null,

    @SerializedName("is_paid")
    var isPaid: Boolean? = false,

    @SuppressLint("SimpleDateFormat") @SerializedName("day_added")
    var dayAdded: String? = SimpleDateFormat("dd/MM/yyyy").format(Date())
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readBoolean(),
        parcel.readString(),
    ) {
    }

    @JvmName("remainVolume")
    fun getRemainingVolume() = deliveredVolume?.let { receivedVolume?.minus(it) }

    @JvmName("amountWillPay")
    fun getAmountPay() = unitPrice?.let { deliveredLeaves?.times(it) }
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nancostDataUid)
        parcel.writeValue(receivedVolume)
        parcel.writeValue(deliveredLeaves)
        parcel.writeValue(deliveredVolume)
        parcel.writeValue(remainVolume)
        parcel.writeValue(unitPrice)
        parcel.writeValue(amountWillPay)
        parcel.writeValue(isPaid)
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