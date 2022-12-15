package com.example.nancost.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "nancost_table")
data class Nancost (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("nancost_name")
    @ColumnInfo(name = "nancost_name")
    var nancostName: String? = null,

    @SerializedName("received_volume")
    @ColumnInfo(name = "received_volume")
    var receivedVolume: Double? = null,

    @SerializedName("delivered_leaves")
    @ColumnInfo(name = "delivered_leaves")
    var deliveredLeaves: Int? = null,

    @SerializedName("delivered_volume")
    @ColumnInfo(name = "delivered_volume")
    var deliveredVolume: Double? = null,

    @SerializedName("remain_volume")
    @ColumnInfo(name = "remain_volume")
    var remainVolume: Double? = null,

    @SerializedName("unit_price")
    @ColumnInfo(name = "unit_price")
    var unitPrice: Double = 8000.0,

    @SerializedName("amount_will_pay")
    @ColumnInfo(name = "amount_will_pay")
    var amountWillPay: Double? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readDouble(),
        parcel.readValue(Double::class.java.classLoader) as? Double
    ) {
    }

    @JvmName("getRemainingVolume1")
    fun getRemainingVolume() = deliveredVolume?.let { receivedVolume?.minus(it) }
    @JvmName("getAmountPay1")
    fun getAmountPay() = deliveredLeaves?.times(unitPrice)
    override fun writeToParcel(dest: Parcel, flags: Int) {
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