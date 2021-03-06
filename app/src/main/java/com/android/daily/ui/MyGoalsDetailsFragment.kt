package com.android.daily.ui


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.android.daily.R
import com.android.daily.ui.MyGoalsDetailsFragmentArgs.fromBundle
import kotlinx.android.synthetic.main.fragment_my_goals_details.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.android.daily.repository.model.TaskData
import com.android.daily.ui.adapters.TasksClickListener
import com.android.daily.ui.adapters.TasksListAdapter
import com.android.daily.utilities.CommonUtils
import com.android.daily.utilities.CommonUtils.Companion.animateTextView
import com.android.daily.viewModel.GoalDetailsViewModel
import com.android.daily.vo.Status
import dagger.android.support.DaggerFragment
import org.joda.time.Days
import org.joda.time.LocalDate
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class MyGoalsDetailsFragment : DaggerFragment(), TaskBottomSheetDialog.OnTaskCompleteClicklistener {

    @Inject
    lateinit var viewmodelfactory: ViewModelProvider.Factory

    private val goal by lazy {
        arguments?.let { fromBundle(it).goalId }
    }

    private lateinit var mView: View
    private lateinit var taskAdapter: TasksListAdapter
    private val taskClickListener: TasksClickListener = this::onTaskClicked
    private var taskBottomSheetDialog: TaskBottomSheetDialog? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_goals_details, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.showToolbar()
        getMainActivity()?.setToolBarTitle(goal!!.n.toUpperCase())
        configureRecyclerView()
        getTaskDetails()
        setRemainingDays()
        goal_description_details_text_view.text = goal?.d
        add_task_details_button.setOnClickListener {
            val navigationDirections = goal?.let { it1 -> MyGoalsDetailsFragmentDirections.actionMyGoalsDetailsFragmentToAddTaskFragment(it1) }
            if (navigationDirections != null) {
                findNavController().navigate(navigationDirections)
            }
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
        var remainingDays = Days.daysBetween(LocalDate.now(), LocalDate(goal?.dd)).days
        if (remainingDays < 0)
            remainingDays = 0
        animateTextView(0, remainingDays, no_days_details_text_view)
    }

    private fun getTaskDetails() {
        val viewModel = ViewModelProviders.of(this, viewmodelfactory).get(GoalDetailsViewModel::class.java)
        viewModel.getTasks(goal!!.gid).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.e("Error loading tasks %s", it.message)
                } else if (it.status == Status.SUCCESS) {
                    sortTasksAccordingDates(it.data)?.let { it1 ->
                        taskAdapter.setData(it1)
                    }
                    CommonUtils.applyLayoutAnimations(goal_details_constraint_layout)
                    setTasksDetails(it.data)
                }
            }

        })
    }

    private fun sortTasksAccordingDates(data: List<TaskData>?): MutableList<TaskData>? {
        val tasks = data?.toMutableList()
        tasks?.sortWith(Comparator { o1: TaskData, o2: TaskData ->
            LocalDate(o1.dd).compareTo(LocalDate(o2.dd))
        })
        return tasks
    }

    private fun setTasksDetails(tasksList: List<TaskData>?) {
        animateTextView(0, getCompletedTasks(tasksList), task_completed_number_details_text_view)
        tasksList?.size?.let { it1 -> animateTextView(0, it1, total_tasks_details_text_view) }
    }

    private fun getCompletedTasks(tasksList: List<TaskData>?): Int {
        var completedTasks: Int = 0
        if (tasksList != null) {
            for (task in tasksList) {
                if (task.c)
                    completedTasks++
            }
        }
        return completedTasks
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


    private fun onTaskClicked(taskData: TaskData) {
        taskBottomSheetDialog = TaskBottomSheetDialog(taskData, this)
        taskBottomSheetDialog?.show(childFragmentManager, "tasksheetdialog")

    }

    override fun onTaskCompleteClicked(taskId: String) {
        val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AlertDialog.Builder(context!!, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            AlertDialog.Builder(context!!)
        }
        builder.setTitle(getString(R.string.complete_task))
                .setMessage(R.string.complete_task_user_prompt)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    completeTask(taskId)
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    // do nothing
                }
                .show()
    }


    private fun completeTask(taskId: String) {
        val viewModel = ViewModelProviders.of(this, viewmodelfactory).get(GoalDetailsViewModel::class.java)
        viewModel.setTaskCompleteStatus(taskId).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR)
                    Timber.e("Error in completing the task for task id %s and error is %s", taskId, it.message)
                else if (it.status == Status.SUCCESS) {
                    Timber.i("Successfully task completed")
                    taskBottomSheetDialog?.dismiss()
                    getTaskDetails()
                }
            }
        })
    }

}
