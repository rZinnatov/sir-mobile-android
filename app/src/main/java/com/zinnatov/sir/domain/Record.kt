package com.zinnatov.sir.domain

import android.os.Parcel
import android.os.Parcelable
import com.zinnatov.sir.util.optDouble
import com.zinnatov.sir.util.optInt

class Record(
        var id: String,
        val name: String,
        val date: String?,
        val inventoryId: String?,
        val room: Int?,
        val amount: Int?,
        val price: Double?,
        val comment: String?
) : Parcelable {
    constructor(parcel: Parcel)
        : this(
            parcel.readString(), // id
            parcel.readString(), // name
            parcel.readString(), // date
            parcel.readString(), // inventoryId
            parcel.optInt(), // room
            parcel.optInt(), // amount
            parcel.optDouble(), // price
            parcel.readString() // comment
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(id)
        dest?.writeString(name)
        dest?.writeString(date)
        dest?.writeString(inventoryId)
        dest?.writeValue(room)
        dest?.writeValue(amount)
        dest?.writeValue(price)
        dest?.writeString(comment)
    }
    companion object {
        @JvmField  val CREATOR: Parcelable.Creator<Record> = object : Parcelable.Creator<Record> {
            override fun createFromParcel(parcel: Parcel): Record {
                return Record(
                        parcel.readString(), // id
                        parcel.readString(), // name
                        parcel.readString(), // date
                        parcel.readString(), // inventoryId
                        parcel.optInt(), // room
                        parcel.optInt(), // amount
                        parcel.optDouble(), // price
                        parcel.readString() // comment
                )
            }

            override fun newArray(size: Int): Array<Record?> {
                return arrayOfNulls(size)
            }
        }
    }
}