package com.android.daily.ui

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.android.daily.R
import com.android.daily.utilities.CommonUtils
import java.text.DateFormat
import java.text.SimpleDateFormat

import java.util.*
import kotlin.collections.ArrayList


class AddReminderDialog : AppCompatDialogFragment() {

    private var selectedReminderDateInMillis: Long = 0
    private lateinit var addReminderListener: AddReminderBottomSheetListener
    private var dialogView: View? = null
    private val calendar: Calendar = Calendar.getInstance()
    private var parentConstraintLayout: ConstraintLayout? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater
        dialogView = inflater?.inflate(R.layout.add_reminder_bottom_dialog, null)
        builder.setView(dialogView)
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    //Do nothing
                }).setPositiveButton("Ok", DialogInterface.OnClickListener { dialogInterface: DialogInterface, i: Int ->
                    sendSelectedReminderData()
                })

        initializeView()
        return builder.create()
    }

    private fun sendSelectedReminderData() {
        val intent = Intent()
        intent.putExtra(REMINDER_DATA_KEY, selectedReminderDateInMillis)
        targetFragment?.onActivityResult(targetRequestCode, REMINDER_REQUEST_CODE, intent)
    }

    private fun initializeView() {
        val todayReminderButton: Button? = dialogView?.findViewById(R.id.today_reminder_button)
        val tomReminderButton: Button? = dialogView?.findViewById(R.id.tommorow_reminder_button)
        val customReminderButton: Button? = dialogView?.findViewById(R.id.custom_reminder_button)
        val setReminderTimeButton: Button? = dialogView?.findViewById(R.id.set_time_reminder_button)
        val setReminderGroup: Group? = dialogView?.findViewById(R.id.reminder_set_group)
        val selectedTimeGroup: Group? = dialogView?.findViewById(R.id.selected_timne_group)
        parentConstraintLayout = dialogView?.findViewById(R.id.set_reminder_constraint_layout)
        setReminderGroup?.visibility = View.GONE
        selectedTimeGroup?.visibility = View.GONE
        todayReminderButton?.setOnClickListener {
            todayReminderButton.isSelected = true
            todayReminderButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
            tomReminderButton?.isSelected = false
            tomReminderButton?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            customReminderButton?.isSelected = false
            customReminderButton?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        }

        tomReminderButton?.setOnClickListener {
            todayReminderButton?.isSelected = false
            todayReminderButton?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            tomReminderButton.isSelected = true
            tomReminderButton.setTextColor(ContextCompat.getColor(context!!, R.color.colorAccent))
            customReminderButton?.isSelected = false
            customReminderButton?.setTextColor(ContextCompat.getColor(context!!, R.color.black))
        }

        setReminderTimeButton?.setOnClickListener { showTimePickerDialog() }
        customReminderButton?.setOnClickListener { startDatePickerDialog() }
        setSpinnerData()

    }


    fun setSpinnerData() {
        val spinner: Spinner? = dialogView?.findViewById(R.id.reminder_day_spinner)
        val data = ArrayList<String>()
        data.add("Day")
        data.add("Week")
        data.add("Month")
        val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_dropdown_item, data)
        spinner?.adapter = adapter
    }


    override fun onAttachFragment(childFragment: Fragment) {
        super.onAttachFragment(childFragment)
        addReminderListener = childFragment as AddReminderBottomSheetListener
    }


    private fun startDatePickerDialog() {
        val currentYear = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            calendar.set(year, monthOfYear, dayOfMonth)
            enableSelectedReminderGroup(calendar.time)
        }, currentYear, month, day)
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }

    private fun enableSelectedReminderGroup(time: Date?) {
        // Display Selected date in textbox
        val group: Group? = dialogView?.findViewById(R.id.one_time_reminder_date_group)
        val setReminderGroup: Group? = dialogView?.findViewById(R.id.reminder_set_group)

        group?.visibility = View.INVISIBLE
        setReminderGroup?.visibility = View.VISIBLE
        parentConstraintLayout?.let { CommonUtils.applyLayoutAnimations(it) }
        val selectedDateTextView: TextView? = dialogView?.findViewById(R.id.selected_reminder_date_textview)
        val removeSelectedReminderImageView: AppCompatImageView? = dialogView?.findViewById(R.id.remove_reminder_imageview)
        removeSelectedReminderImageView?.setOnClickListener {
            //disable the slected reminder layout
            group?.visibility = View.VISIBLE
            setReminderGroup?.visibility = View.GONE
            parentConstraintLayout?.let { CommonUtils.applyLayoutAnimations(it) }
            selectedReminderDateInMillis = 0
        }
        val dateString = SimpleDateFormat("EEE, MMMM d, yyyy").format(time)
        selectedDateTextView?.text = dateString

    }

    private fun showTimePickerDialog() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val mTimePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { timePicker: TimePicker, selectedHour: Int, selectedMinute: Int ->
            calendar.set(Calendar.HOUR_OF_DAY, selectedHour)
            calendar.set(Calendar.MINUTE, selectedMinute)
            calendar.set(Calendar.SECOND, 0)
            updateTimeText()
        }, hour, minute, true)
        mTimePicker.setTitle("Select Time")
        mTimePicker.show()
    }

    private fun updateTimeText() {
        val selectedTimeGroup: Group? = dialogView?.findViewById(R.id.selected_timne_group)
        val setReminderTimeButton: Button? = dialogView?.findViewById(R.id.set_time_reminder_button)
        selectedTimeGroup?.visibility = View.VISIBLE
        setReminderTimeButton?.visibility = View.INVISIBLE
        parentConstraintLayout?.let { CommonUtils.applyLayoutAnimations(it) }
        val selectedTimeTextView: TextView? = dialogView?.findViewById(R.id.selected_time_textView)
        val removeSelectedTimeImageView: ImageView? = dialogView?.findViewById(R.id.remove_selected_time_imageview)
        selectedTimeTextView?.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.time)
        removeSelectedTimeImageView?.setOnClickListener {
            setReminderTimeButton?.visibility = View.VISIBLE
            selectedTimeGroup?.visibility = View.GONE
            selectedReminderDateInMillis = 0
        }
        parentConstraintLayout?.let { CommonUtils.applyLayoutAnimations(it) }

        selectedReminderDateInMillis = calendar.timeInMillis
    }


    interface AddReminderBottomSheetListener {
        fun onRemindModeSelected()
    }

    companion object {
        const val REMINDER_DATA_KEY: String = "reminderKey"
        const val REMINDER_REQUEST_CODE: Int = 100
    }


}