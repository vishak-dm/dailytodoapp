package com.android.daily.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.daily.utilities.AppConstants
import com.android.daily.utilities.NotificationUtil
import com.android.daily.utilities.PrefUtil

class TimerNotificationActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            AppConstants.ACTION_STOP -> {
                TaskTimerFragment.removeAlarm(context)
                PrefUtil.setTimerState(TaskTimerFragment.TimerState.Stopped, context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE -> {
                var secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = TaskTimerFragment.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecondsRemaining(secondsRemaining, context)

                TaskTimerFragment.removeAlarm(context)
                PrefUtil.setTimerState(TaskTimerFragment.TimerState.Paused, context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME -> {
                val secondsRemaining = PrefUtil.getSecondsRemaining(context)
                val wakeUpTime = TaskTimerFragment.setAlarm(context, TaskTimerFragment.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TaskTimerFragment.TimerState.Running, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
            AppConstants.ACTION_START -> {
                val minutesRemaining = PrefUtil.getTimerLength(context)
                val secondsRemaining = minutesRemaining * 60L
                val wakeUpTime = TaskTimerFragment.setAlarm(context, TaskTimerFragment.nowSeconds, secondsRemaining)
                PrefUtil.setTimerState(TaskTimerFragment.TimerState.Running, context)
                PrefUtil.setSecondsRemaining(secondsRemaining, context)
                NotificationUtil.showTimerRunning(context, wakeUpTime)
            }
        }
    }
}