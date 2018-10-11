package com.android.daily.utilities

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.widget.TextView
import com.android.daily.R
import org.joda.time.Days
import org.joda.time.LocalDate
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class CommonUtils {
    companion object {
        fun getIntialsFromString(string: String): String {
            val initials = StringBuilder()
            var i = 0
            for (s in string.split(" ")) {
                initials.append(s[0])
            }
            return initials.substring(0, 2).toString()
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

        fun getDaysBetweenTwoDays(currentMillis: Long, selectedMillis: Long): Int {
            val diff = Math.abs(currentMillis - selectedMillis)
            return TimeUnit.MILLISECONDS.toDays(diff).toInt()
        }

        fun getReadableDaysRemainingString(context: Context, dueDate: Long): String {
            val daysRemaining = Days.daysBetween(LocalDate.now(), LocalDate(dueDate)).days
            return when {
                daysRemaining == 0 -> context.getString(R.string.today)
                daysRemaining == 1 -> context.getString(R.string.tommorow)
                daysRemaining < 0 -> String.format(context.getString(R.string.due_on), LocalDate(dueDate).toString("MMM dd, yyyy"))
                else -> String.format(context.getString(R.string.days_remaining), daysRemaining)
            }
        }

        fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView) {

            val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
            valueAnimator.duration = 700

            valueAnimator.addUpdateListener { valueAnimator -> textview.text = valueAnimator.animatedValue.toString() }
            valueAnimator.start()
        }
    }
}