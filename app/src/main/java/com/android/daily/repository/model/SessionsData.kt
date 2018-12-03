package com.android.daily.repository.model

import android.os.Parcel
import android.os.Parcelable

/*
* n = name of the session
* d = description of the session
* t = time at which session was created
* l = length of the session
*/

data class SessionsData(var n: String = "", var d: String = "", var t: Long = 0L, var l: Long = 0L) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readLong()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(n)
        parcel.writeString(d)
        parcel.writeLong(t)
        parcel.writeLong(l)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SessionsData> {
        override fun createFromParcel(parcel: Parcel): SessionsData {
            return SessionsData(parcel)
        }

        override fun newArray(size: Int): Array<SessionsData?> {
            return arrayOfNulls(size)
        }
    }
}