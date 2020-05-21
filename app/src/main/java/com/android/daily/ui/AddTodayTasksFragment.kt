package com.android.daily.ui


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.android.daily.R
import com.android.daily.repository.model.GoalsData
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.AddTaskViewModel
import kotlinx.android.synthetic.main.add_today_tasks_fragment.*
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.daily.vo.Status
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_add_task.*
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject


class AddTodayTasksFragment : DaggerFragment(), AdapterView.OnItemSelectedListener {

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    lateinit var mView: View
    private lateinit var selectedGoal: GoalsData
    private var listOfGoals = ArrayList<GoalsData>()
    private lateinit var addTaskViewModel: AddTaskViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.add_today_tasks_fragment, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.setToolBarTitle(getString(R.string.add_task))
        goal_name_spinner.onItemSelectedListener = this
        addTaskViewModel = ViewModelProviders.of(this, viewmodelFactory).get(AddTaskViewModel::class.java)
        getCurrentGoals()
        add_today_task_button.setOnClickListener {
            add_today_task_progressbar.visibility = View.VISIBLE
            add_today_task_button.visibility = View.GONE
            val taskName = today_task_name_text_input_layout.editText?.text.toString()
            val taskDescription = today_task_description_text_input_layout.editText?.text.toString()
            if (validateInput(taskName, taskDescription)) {
                //as for now do nothing
                addTaskViewModel.addTask(taskName, taskDescription, DateTime.now().millis, selectedGoal.gid).observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    if (it != null) {
                        if (it.status == Status.ERROR) {
                            Timber.i("Error in adding today task %s", it.message)
                            Snackbar.make(mView, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                        } else if (it.status == Status.SUCCESS) {
                            Timber.i("Success  in adding todays task")
                            //just pop from the stack
                            findNavController().popBackStack()
                        }
                        add_today_task_progressbar.visibility = View.GONE
                        add_today_task_button.visibility = View.VISIBLE
                    }
                })
            }
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
        return true
    }

    private fun getCurrentGoals() {
        addTaskViewModel.getAllGoalsForUser().observe(viewLifecycleOwner, Observer {
            if (it?.data != null) {
                listOfGoals = it.data as ArrayList<GoalsData>
                val listOfGoalNames = ArrayList<String>()
                for (goal in listOfGoals) {
                    listOfGoalNames.add(goal.n.capitalize())
                }
                val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_dropdown_item, listOfGoalNames)
                goal_name_spinner.adapter = adapter // this wil
            }
        })
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        selectedGoal = listOfGoals[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


}
