package com.example.keskonmange.main

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

class Resto() : Parcelable {
    var id: Int = 0
    var type: String? = null
    var name: String? = null
    var votes: Int? = 0
    var order: Int? = 0
    var vetto: Boolean? = false

    @SuppressLint("NewApi")
    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        type = parcel.readString()
        name = parcel.readString()
        votes = parcel.readInt()
        order = parcel.readInt()
        vetto = parcel.readBoolean()
    }

    override fun toString(): String {
        return "$name $type $votes $vetto"
    }
    @SuppressLint("NewApi")
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(type)
        parcel.writeString(name)
        parcel.writeInt(votes!!)
        parcel.writeInt(order!!)
        parcel.writeBoolean(vetto!!)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Resto> {
        override fun createFromParcel(parcel: Parcel): Resto {
            return Resto(parcel)
        }

        override fun newArray(size: Int): Array<Resto?> {
            return arrayOfNulls(size)
        }
    }
}
