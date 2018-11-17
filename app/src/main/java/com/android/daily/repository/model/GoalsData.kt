package com.android.daily.repository.model

import android.os.Parcel
import android.os.Parcelable


data class GoalsData(var n: String = "", var d: String = "", var dd: Long = 0L,
                     var uid: String = "",
                     var gid: String = "", var gt: Int = 0, var gc: Int = 0, var c: Boolean = false) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(n)
        parcel.writeString(d)
        parcel.writeLong(dd)
        parcel.writeString(uid)
        parcel.writeString(gid)
        parcel.writeInt(gt)
        parcel.writeInt(gc)
        parcel.writeByte(if (c) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoalsData> {
        override fun createFromParcel(parcel: Parcel): GoalsData {
            return GoalsData(parcel)
        }

        override fun newArray(size: Int): Array<GoalsData?> {
            return arrayOfNulls(size)
        }
    }

}
