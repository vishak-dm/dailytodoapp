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
import androidx.navigation.Navigation
import com.android.daily.R
import com.android.daily.utilities.InjectorUtils
import com.android.daily.utilities.extenstions.clearErrorOnTextChange
import com.android.daily.viewModel.AddGoalViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_add_goals.*
import timber.log.Timber
import java.util.*


class AddGoalsFragment : Fragment() {

    private lateinit var mView: View
    private var selectedDateInMills: Long = 0L
    //0 indicares short term goal  and 1 indicates long term goal
    private var goalType: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_add_goals, container, false)
        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.setToolBarTitle(getString(R.string.add_goal))
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goalNametextInputLayout.clearErrorOnTextChange()
        goalDescriptionTextInputLayout.clearErrorOnTextChange()
        setGoalType()
        goal_duedate_button.setOnClickListener {
            startDatePickerDialog()
        }
        addGoalButton.setOnClickListener {
            saveGoals()
        }
    }

    private fun setGoalType() {
        when (goal_type_radio_group.checkedRadioButtonId) {
            short_term_radio_button.id -> goalType = 0
            long_term_radio_button.id -> goalType = 1
        }

    }

    private fun saveGoals() {
        val goalName = goalNametextInputLayout.editText?.text.toString()
        val goalDescription = goalDescriptionTextInputLayout.editText?.text.toString()
        if (validateInput(goalName, goalDescription)) {
            add_goal_progress.visibility = View.VISIBLE
            addGoalButton.visibility = View.GONE
            Timber.i("validated  add goal inputs")
            val viewModel = ViewModelProviders.of(this, InjectorUtils.provideAddGoalsViewModelFactory()).get(AddGoalViewModel::class.java)
            viewModel.saveGoal(goalName, goalDescription, selectedDateInMills ,goalType).observe(viewLifecycleOwner, android.arch.lifecycle.Observer {
                if (it != null) {
                    if (it.status == Status.ERROR) {
                        Timber.i("Error while adding the goals to database %s", it.message)
                        Snackbar.make(mView, getString(R.string.error_adding_goal), Snackbar.LENGTH_SHORT).show()
                    } else if (it.status == Status.SUCCESS) {
                        Timber.i("Successfully added the goals to database")
                        Navigation.findNavController(mView).popBackStack()
                    }
                    add_goal_progress.visibility = View.GONE
                    addGoalButton.visibility = View.VISIBLE
                }
            })
        }


    }

    private fun validateInput(goalName: String, goalDescription: String): Boolean {
        if (goalName.isEmpty()) {
            goalNametextInputLayout.error = getString(R.string.empty_goal_name)
            return false
        }
        if (goalDescription.isEmpty()) {
            goalDescriptionTextInputLayout.error = getString(R.string.empty_goal_description)
            return false
        }
        if (selectedDateInMills == 0L) {
            Snackbar.make(mView, R.string.empty_due_date, Snackbar.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun startDatePickerDialog() {
        val c = Calendar.getInstance()
        val currentYear = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datePcker = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in textbox
            goal_duedate_button.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            goal_duedate_button.text = StringBuilder()
                    // Month is 0 based so add 1
                    .append(dayOfMonth).append("/").append(monthOfYear + 1).append("/").append(year).append(" ")
            val calendar = Calendar.getInstance()
            calendar.set(year, monthOfYear, dayOfMonth)
            selectedDateInMills = calendar.timeInMillis


        }, currentYear, month, day)
        datePcker.datePicker.minDate = System.currentTimeMillis()
        datePcker.show()
    }


    private fun getMainActivity(): MainActivity? {
        if (activity is MainActivity)
            return activity as MainActivity
        else
            return null
    }


}
