package com.android.daily.ui


import android.annotation.TargetApi
import android.app.AlarmManager
import android.app.PendingIntent
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.ui.TaskTimerFragmentArgs.fromBundle
import com.android.daily.utilities.InjectorUtils
import com.android.daily.utilities.NotificationUtil
import com.android.daily.utilities.PrefUtil
import com.android.daily.viewModel.TaskTimerViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_task_timer.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


class TaskTimerFragment : Fragment() {

    private lateinit var mView: View
    private val taskDetails by lazy {
        fromBundle(arguments).taskDetails
    }
    private var exitTimer: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_task_timer, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.hideToolbar()
        getMainActivity()?.hideBottomNavigationView()
        fab_start.setOnClickListener { v ->
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }

        fab_pause.setOnClickListener { v ->
            timer?.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }

        fab_stop.setOnClickListener { v ->
            showSaveElapsedTimeDialog()
        }

    }

    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long {
            val wakeUpTime = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeUpTime
        }

        fun removeAlarm(context: Context) {
            val intent = Intent(context, TimerExpiredReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long
            get() = Calendar.getInstance().timeInMillis / 1000
    }

    enum class TimerState {
        Stopped, Paused, Running
    }

    private var timer: CountDownTimer? = null
    private var timerLengthSeconds: Long = 0
    private var timerState = TimerState.Stopped

    private var secondsRemaining: Long = 0


    override fun onResume() {
        super.onResume()
        if (PrefUtil.getCurrentTaskId(context!!).isNotEmpty() && !taskDetails.id.equals(PrefUtil.getCurrentTaskId(context!!))) {
            showConfirmationDialog()
            return
        } else {
            initTimer()
            removeAlarm(context!!)
            NotificationUtil.hideTimerNotification(context!!)
        }
    }

    override fun onPause() {
        super.onPause()
        if (exitTimer) {
            return
        }
        if (timerState == TimerState.Running) {
            timer?.cancel()
            val wakeUpTime = setAlarm(context!!, nowSeconds, secondsRemaining)
            NotificationUtil.showTimerRunning(context!!, wakeUpTime)
            PrefUtil.setCurrentTaskId(taskDetails.id, context!!)
        } else if (timerState == TimerState.Paused) {
            PrefUtil.setCurrentTaskId(taskDetails.id, context!!)
            NotificationUtil.showTimerPaused(context!!)
        }

        PrefUtil.setPreviousTimerLengthSeconds(timerLengthSeconds, context!!)
        PrefUtil.setSecondsRemaining(secondsRemaining, context!!)
        PrefUtil.setTimerState(timerState, context!!)
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(context!!)

        //we don't want to change the length of the timer which is already running
        //if the length was changed in settings while it was backgrounded
        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPreviousTimerLength()

        secondsRemaining = if (timerState == TimerState.Running || timerState == TimerState.Paused)
            PrefUtil.getSecondsRemaining(context!!)
        else
            timerLengthSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(context!!)
        if (alarmSetTime > 0)
            secondsRemaining -= nowSeconds - alarmSetTime

        if (secondsRemaining <= 0)
            onTimerFinished()
        else if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped
        //set the length of the timer to be the one set in SettingsActivity
        //if the length was changed when the timer was running
        setNewTimerLength()
        progress_countdown.progress = 0

        PrefUtil.setSecondsRemaining(timerLengthSeconds, context!!)
        secondsRemaining = timerLengthSeconds
        updateButtons()
        updateCountdownUI()
        PrefUtil.setCurrentTaskId("", context!!)
    }


    private fun startTimer() {
        timerState = TimerState.Running

        timer = object : CountDownTimer(secondsRemaining * 1000, 1000) {
            override fun onFinish() = onTimerFinished()

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerLength(context!!)
        timerLengthSeconds = (lengthInMinutes * 60L)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun setPreviousTimerLength() {
        timerLengthSeconds = PrefUtil.getPreviousTimerLengthSeconds(context!!)
        progress_countdown.max = timerLengthSeconds.toInt()
    }

    private fun updateCountdownUI() {
        val minutesUntilFinished = secondsRemaining / 60
        val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        textView_countdown.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
        progress_countdown.progress = (timerLengthSeconds - secondsRemaining).toInt()
    }

    private fun getElapsedTimeInSeconds(): Long {
        return (((PrefUtil.getTimerLength(context!!) * 60L) - (secondsRemaining)))
    }


    private fun updateButtons() {
        when (timerState) {
            TimerState.Running -> {
                fab_start.isEnabled = false
                fab_pause.isEnabled = true
                fab_stop.isEnabled = true
            }
            TimerState.Stopped -> {
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = false
            }
            TimerState.Paused -> {
                fab_start.isEnabled = true
                fab_pause.isEnabled = false
                fab_stop.isEnabled = true
            }
        }
        setBackgroundTint(fab_start)
        setBackgroundTint(fab_pause)
        setBackgroundTint(fab_stop)
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setBackgroundTint(button: Button) {
        var color: Int = if (button.isEnabled)
            R.color.colorAccent
        else
            R.color.medium_gray

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            button.backgroundTintList = context!!.resources.getColorStateList(color, null)
        } else {
            button.backgroundTintList = context!!.resources.getColorStateList(color)
        }
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }

    private fun showConfirmationDialog() {
        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(context!!, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(context!!)
        }
        builder.setTitle(getString(R.string.start_new_timer))
                .setMessage(R.string.start_new_timer_message)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    onTimerFinished()
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    exitTimer = true
                    findNavController().popBackStack()
                }
                .show()
    }


    private fun showSaveElapsedTimeDialog() {
        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(context!!, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(context!!)
        }
        builder.setTitle(getString(R.string.save_session))
                .setMessage(R.string.save_session_message)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    //add it in the database
                    timer?.cancel()
                    val elapsedTime = getElapsedTimeInSeconds()
                    onTimerFinished()
                    saveSession(elapsedTime)
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    timer?.cancel()
                    onTimerFinished()
                }
                .show()
    }

    private fun saveSession(elapsedTime: Long) {
        val navDirections = TaskTimerFragmentDirections.actionTaskTimerFragmentToTaskSessionFragment(taskDetails, elapsedTime)
        findNavController().navigate(navDirections)
    }


}
