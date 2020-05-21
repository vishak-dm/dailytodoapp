package com.android.daily.ui


import android.app.DatePickerDialog
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.utilities.InjectorUtils
import com.android.daily.utilities.extenstions.clearErrorOnTextChange
import com.android.daily.viewModel.AddTaskViewModel
import com.android.daily.vo.Status
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_task.*
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class AddTaskFragment : DaggerFragment(), AddReminderDialog.AddReminderBottomSheetListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mView: View
    private var selectedDateInMills: Long = 0L

    private val goaldetails by lazy {
        arguments?.let { AddTaskFragmentArgs.fromBundle(it).goaldetails }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_task, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTextWatchers()
        hideBottomNavigation()
        //goal_name_text_view_add_task.text = goaldetails?.n
        getMainActivity()?.setToolBarTitle(getString(R.string.add_task))
        task_duedate_button.setOnClickListener { startDatePickerDialog() }
        add_task_button.setOnClickListener { addTask() }
        remind_me_text_view.setOnClickListener {
            showReminderDialog()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AddReminderDialog.REMINDER_REQUEST_CODE) {
            val selectedReminderInMills = data?.getLongExtra(AddReminderDialog.REMINDER_DATA_KEY, 0)
            val cal = Calendar.getInstance()
            cal.timeInMillis = selectedReminderInMills!!
            val dateString = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.time)
            remind_me_text_view.text = dateString
        }
    }

    private fun showReminderDialog() {
        val addReminderBottomDialog = AddReminderDialog()
        addReminderBottomDialog.setTargetFragment(this, 100)
        addReminderBottomDialog.show(fragmentManager!!, "addReminderBottomDialog")
    }

    private fun startDatePickerDialog() {
        val c = Calendar.getInstance()
        val currentYear = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            task_duedate_button.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            task_duedate_button.text = StringBuilder()
                    // Month is 0 based so add 1
                    .append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year).append(" ")
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            selectedDateInMills = calendar.timeInMillis


        }, currentYear, month, day)
        if (goaldetails!!.dd < System.currentTimeMillis()) {
            datePicker.datePicker.maxDate = System.currentTimeMillis()
        } else {
            datePicker.datePicker.maxDate = goaldetails!!.dd

        }
        datePicker.datePicker.minDate = System.currentTimeMillis()
        datePicker.show()
    }


    private fun addTask() {
        add_task_progressbar.visibility = View.VISIBLE
        add_task_button.visibility = View.GONE
        val taskName = task_name_text_input_layout.editText?.text.toString()
        val taskDescription = task_description_text_input_layout.editText?.text.toString()
        if (validateInput(taskName, taskDescription)) {
            //as for now do nothing
            val viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddTaskViewModel::class.java)
            viewModel.addTask(taskName, taskDescription, selectedDateInMills, goaldetails?.gid).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it != null) {
                    if (it.status == Status.ERROR) {
                        Timber.i("Error in adding task %s", it.message)
                        Snackbar.make(mView, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                    } else if (it.status == Status.SUCCESS) {
                        Timber.i("Success  in adding task")
                        //just pop from the stack
                        findNavController().popBackStack()
                    }
                    add_task_progressbar.visibility = View.GONE
                    add_task_button.visibility = View.VISIBLE
                }
            })
        }
    }


    private fun validateInput(taskName: String, taskDescription: String): Boolean {
        if (taskName.isEmpty()) {
            task_name_text_input_layout.error = getString(R.string.empty_task_name)
            return false
        }
        if (taskDescription.isEmpty()) {
            task_description_text_input_layout.error = getString(R.string.empty_task_description)
            return false
        }
        if (selectedDateInMills == 0L) {
            Snackbar.make(mView, R.string.empty_due_date, Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun onRemindModeSelected() {

    }


    private fun setTextWatchers() {
        task_name_text_input_layout.clearErrorOnTextChange()
        task_description_text_input_layout.clearErrorOnTextChange()
    }

    private fun hideBottomNavigation() {
        getMainActivity()?.hideBottomNavigationView()
    }

    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


}
