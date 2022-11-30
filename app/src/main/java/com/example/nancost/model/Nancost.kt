package com.example.nancost.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "nancost_table")
data class Nancost (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @Expose
    var id: Int,

    @SerializedName("name")
    @ColumnInfo(name = "name")
    var name: String? = null,

    @SerializedName("received_volume")
    @ColumnInfo(name = "received_volume")
    var receivedVolume: Double? = null,

    @SerializedName("delivered_leaves")
    @ColumnInfo(name = "delivered_leaves")
    var deliveredLeaves: Int? = null,

    @SerializedName("delivered_volume")
    @ColumnInfo(name = "delivered_volume")
    var deliveredVolume: Double? = null,

    @SerializedName("remaining_volume")
    @ColumnInfo(name = "remaining_volume")
    var remainingVolume: Double? = null,

    @SerializedName("unit_price")
    @ColumnInfo(name = "unit_price")
    var unitPrice: Double? = null,

    @SerializedName("amount_pay")
    @ColumnInfo(name = "amount_pay")
    var amountPay: Double? = null,
) : Parcelable