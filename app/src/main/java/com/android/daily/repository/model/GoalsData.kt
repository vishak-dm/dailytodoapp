package com.android.daily.repository.model

import android.os.Parcel
import android.os.Parcelable


data class GoalsData(var goalName: String = "", var goalDescription: String = "", var dueDate: Long = 0L,
                     var uid: String = "",
                     var goalId: String = "") : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(goalName)
        parcel.writeString(goalDescription)
        parcel.writeLong(dueDate)
        parcel.writeString(uid)
        parcel.writeString(goalId)
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
