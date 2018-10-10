package com.android.daily.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.daily.R
import com.android.daily.ui.MyGoalsDetailsFragmentArgs.fromBundle
import kotlinx.android.synthetic.main.fragment_my_goals_details.*
import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.Snackbar
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.android.daily.repository.model.TaskData
import com.android.daily.ui.adapters.TasksClickListener
import com.android.daily.ui.adapters.TasksListAdapter
import com.android.daily.utilities.CommonUtils
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.GoalDetailsViewModel
import com.android.daily.vo.Status
import org.joda.time.Days
import org.joda.time.LocalDate
import timber.log.Timber
import java.util.*


class MyGoalsDetailsFragment : Fragment() {
    private val goal by lazy {
        fromBundle(arguments).goalId
    }

    private lateinit var mView: View
    private lateinit var taskAdapter: TasksListAdapter
    private val taskClickListener: TasksClickListener = this::onTaskClicked


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_goals_details, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.hideBottomNavigationView()
        configureRecyclerView()
        getTaskDetails()
        setRemainingDays()
        goal_name_details_text_view.text = goal.goalName
        goal_description_details_text_view.text = goal.goalDescription
        add_task_details_button.setOnClickListener {
            val navigationDirections = MyGoalsDetailsFragmentDirections.actionMyGoalsDetailsFragmentToAddTaskFragment(goal)
            findNavController().navigate(navigationDirections)
        }

    }

    private fun configureRecyclerView() {
        taskAdapter = TasksListAdapter(context!!, emptyList(), taskClickListener)
        val mLayoutManager = LinearLayoutManager(context)
        tasks_list_recycler_view.layoutManager = mLayoutManager
        tasks_list_recycler_view.itemAnimator = DefaultItemAnimator()
        tasks_list_recycler_view.adapter = taskAdapter
    }

    private fun setRemainingDays() {
        val remainingDays = CommonUtils.getDaysBetweenTwoDays(Calendar.getInstance().timeInMillis, goal.dueDate)
        animateTextView(0, Days.daysBetween(LocalDate.now(),LocalDate(goal.dueDate)).days, no_days_details_text_view)

    }

    private fun getTaskDetails() {
        val viewModel = ViewModelProviders.of(this, InjectorUtils.provideGoalDetailsViewModelFactory()).get(GoalDetailsViewModel::class.java)
        viewModel.getTasks(goal.goalId).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.e("Error loading tasks %s", it.message)
                    Snackbar.make(mView, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                } else if (it.status == Status.SUCCESS) {
                    it.data?.let { it1 -> taskAdapter.setData(it1) }
                    setTasksDetails(it.data)
                }
            }

        })
    }

    private fun setTasksDetails(tasksList: List<TaskData>?) {
        animateTextView(0, getCompletedTasks(tasksList), task_completed_number_details_text_view)
        tasksList?.size?.let { it1 -> animateTextView(0, it1, total_tasks_details_text_view) }
    }

    private fun getCompletedTasks(tasksList: List<TaskData>?): Int {
        var completedTasks: Int = 0
        if (tasksList != null) {
            for (task in tasksList) {
                if (task.isCompleted)
                    completedTasks++
            }
        }
        return completedTasks
    }


    fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView) {

        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
        valueAnimator.duration = 700

        valueAnimator.addUpdateListener { valueAnimator -> textview.text = valueAnimator.animatedValue.toString() }
        valueAnimator.start()
    }


    private fun getMainActivity(): MainActivity? {
        if (activity is MainActivity)
            return activity as MainActivity
        else
            return null
    }


    private fun onTaskClicked(taskData: TaskData) {
    }


}