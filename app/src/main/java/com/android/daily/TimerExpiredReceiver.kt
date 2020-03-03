package com.android.daily

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.daily.ui.TaskTimerFragment
import com.android.daily.utilities.NotificationUtil
import com.android.daily.utilities.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(TaskTimerFragment.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}