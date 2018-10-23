package com.android.daily.repository.model

import android.os.Parcel
import android.os.Parcelable


data class TaskData(var taskName: String = "", var taskDescription: String = "", var taskDueDate: Long = 0L,
                    var taskId: String = "", var goalId: String = "", var isCompleted: Boolean = false , var isMit:Boolean = false) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readByte()!=0.toByte()
            ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(taskName)
        parcel.writeString(taskDescription)
        parcel.writeLong(taskDueDate)
        parcel.writeString(taskId)
        parcel.writeString(goalId)
        parcel.writeByte(if (isCompleted) 1 else 0)
        parcel.writeByte(if (isMit) 1 else 0)
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