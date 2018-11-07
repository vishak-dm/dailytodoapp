package com.android.daily.repository.model

import android.os.Parcel
import android.os.Parcelable

data class NotesData constructor(var t: String = "", var c: String = "", var id: String = "", var d: Long = 0L, var nc: Int = 0, var nl: List<String> = ArrayList()) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readInt(),
            parcel.createStringArrayList()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(t)
        parcel.writeString(c)
        parcel.writeString(id)
        parcel.writeLong(d)
        parcel.writeInt(nc)
        parcel.writeStringList(nl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotesData> {
        override fun createFromParcel(parcel: Parcel): NotesData {
            return NotesData(parcel)
        }

        override fun newArray(size: Int): Array<NotesData?> {
            return arrayOfNulls(size)
        }
    }


}