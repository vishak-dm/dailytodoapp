package com.android.daily.ui


import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.utilities.InjectorUtils
import com.android.daily.utilities.extenstions.clearErrorOnTextChange
import com.android.daily.viewModel.AddTaskViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_add_task.*
import org.joda.time.DateTime
import org.joda.time.LocalDate
import timber.log.Timber
import java.util.*

class AddTaskFragment : Fragment() {

    private lateinit var mView: View
    private var selectedDateInMills: Long = 0L

    private val goaldetails by lazy {
        AddTaskFragmentArgs.fromBundle(arguments).goaldetails
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
        goal_name_text_view_add_task.text = goaldetails.goalName
        getMainActivity()?.setToolBarTitle(getString(R.string.add_task))
        task_duedate_button.setOnClickListener { startDatePickerDialog() }
        add_task_button.setOnClickListener { addTask() }
    }

    private fun startDatePickerDialog() {
        val c = Calendar.getInstance()
        val currentYear = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePcker = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            task_duedate_button.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            task_duedate_button.text = StringBuilder()
                    // Month is 0 based so add 1
                    .append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year).append(" ")
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            selectedDateInMills = calendar.timeInMillis


        }, currentYear, month, day)
        datePcker.datePicker.maxDate = goaldetails.dueDate
        datePcker.datePicker.minDate = System.currentTimeMillis()
        datePcker.show()
    }


    private fun addTask() {
        add_task_progressbar.visibility = View.VISIBLE
        add_task_button.visibility = View.GONE
        val taskName = task_name_text_input_layout.editText?.text.toString()
        val taskDescription = task_description_text_input_layout.editText?.text.toString()
        if (validateInput(taskName, taskDescription)) {
            //as for now do nothing
            val viewModel = ViewModelProviders.of(this, InjectorUtils.provideAddTaskViewModelFactory()).get(AddTaskViewModel::class.java)
            viewModel.addTask(taskName, taskDescription, selectedDateInMills, goaldetails).observe(viewLifecycleOwner, android.arch.lifecycle.Observer {
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


    private fun setTextWatchers() {
        task_name_text_input_layout.clearErrorOnTextChange()
        task_description_text_input_layout.clearErrorOnTextChange()
    }

    private fun hideBottomNavigation() {
        getMainActivity()?.hideBottomNavigationView()
    }

    private fun getMainActivity(): MainActivity? {
        if (activity is MainActivity)
            return activity as MainActivity
        else
            return null
    }


}
