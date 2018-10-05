package com.android.daily.utilities

import android.content.res.Resources
import android.util.TypedValue
import java.text.SimpleDateFormat
import java.util.*


class CommonUtils {
    companion object {
        fun getIntialsFromString(string: String): String {
            val initials = StringBuilder()
            var i = 0
            for (s in string.split(" ")) {
                initials.append(s[0])
            }
            return initials.substring(0,2).toString()
        }

        /**
         * Converting dp to pixel
         */
        fun dpToPx(dp: Int, resources: Resources): Int {
            return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics))
        }


        fun getDateFrom(milliSeconds: Long): String {
            val formatter = SimpleDateFormat("MMM dd, yyyy")
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds
            return formatter.format(calendar.time)
        }
    }
}