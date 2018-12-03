package com.android.daily.repository.model

import android.os.Parcel
import android.os.Parcelable


data class TaskData(var n: String = "", var d: String = "", var dd: Long = 0L,
                    var id: String = "", var gid: String = "", var c: Boolean = false, var m: Boolean = false, var sl: List<SessionsData> = ArrayList()) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte() != 0.toByte(),
            parcel.createTypedArrayList(SessionsData)) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(n)
        parcel.writeString(d)
        parcel.writeLong(dd)
        parcel.writeString(id)
        parcel.writeString(gid)
        parcel.writeByte(if (c) 1 else 0)
        parcel.writeByte(if (m) 1 else 0)
        parcel.writeTypedList(sl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TaskData> {
        override fun createFromParcel(parcel: Parcel): TaskData {
            return TaskData(parcel)
        }

        override fun newArray(size: Int): Array<TaskData?> {
            return arrayOfNulls(size)
        }
    }


}