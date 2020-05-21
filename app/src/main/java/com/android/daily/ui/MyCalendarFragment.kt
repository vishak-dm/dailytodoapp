package com.android.daily.ui


import android.os.Build
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.repository.model.TaskData
import com.android.daily.ui.adapters.CalendarTasksAdapter
import com.android.daily.ui.adapters.TaskClickListener
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.GoalDetailsViewModel
import com.android.daily.viewModel.MyCalendarViewModel
import com.android.daily.vo.Status
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_my_calendar.*
import org.joda.time.DateTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class MyCalendarFragment : DaggerFragment(), TaskBottomSheetDialog.OnTaskCompleteClicklistener {

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    private lateinit var mView: View
    private var selectedDateInMills: Long = 0
    private lateinit var myCalendarViewModel: MyCalendarViewModel
    private var tasksList: MutableList<TaskData> = mutableListOf()
    private lateinit var calendarTaskAdapter: CalendarTasksAdapter
    private val taskClickListener: TaskClickListener = this::onTaskClicked
    private lateinit var taskBottomSheetDialog: TaskBottomSheetDialog


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_calendar, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.hideBackButton()
        getMainActivity()?.showToolbar()
        getMainActivity()?.showBottomNavigationView()
        getMainActivity()?.setToolBarTitle(getString(R.string.myCalendar))
        configureRecyclerView()
        //inialize the view model
        myCalendarViewModel = ViewModelProviders.of(this, viewmodelFactory).get(MyCalendarViewModel::class.java)
        my_calendar_view.minDate = DateTime.now().millis
        getTasksForSelectedDate(DateTime.now().millis)
        my_calendar_view.setOnDateChangeListener { view, year, month, dayOfMonth ->
            my_calendar_progress_bar.visibility = View.VISIBLE
            tasksList.clear()
            calendarTaskAdapter.setData(tasksList)
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDateInMills = calendar.timeInMillis
            //schedule a network call after some time
            getTasksForSelectedDate(selectedDateInMills)
        }


    }

    private fun getTasksForSelectedDate(selectedDateInMills: Long) {
        val selectedDate = DateTime(selectedDateInMills)
        myCalendarViewModel.getSelectedDateTasks(selectedDate.withTimeAtStartOfDay().millis, selectedDate.plusDays(1).withTimeAtStartOfDay().millis)
                .observe(viewLifecycleOwner, Observer {
                    if (it != null) {
                        if (it.status == Status.ERROR) {
                            Timber.i(it.message)
                        } else if (it.status == Status.SUCCESS) {
                            if (it.data != null) {
                                tasksList = it.data as MutableList<TaskData>
                                if (tasksList.isNotEmpty())
                                    calendarTaskAdapter.setData(tasksList)
                            }
                        }
                        my_calendar_progress_bar.visibility = View.GONE

                    }
                })
    }

    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


    private fun configureRecyclerView() {
        calendarTaskAdapter = CalendarTasksAdapter(context!!, Collections.emptyList(), taskClickListener)
        val mLayoutManager = LinearLayoutManager(context)
        calendar_recycler_view.layoutManager = mLayoutManager
        calendar_recycler_view.itemAnimator = DefaultItemAnimator()
        calendar_recycler_view.adapter = calendarTaskAdapter
    }

    private fun onTaskClicked(task: TaskData) {
        taskBottomSheetDialog = TaskBottomSheetDialog(task, this)
        taskBottomSheetDialog.show(childFragmentManager, "taskBottomSheetDialog")
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
        val viewModel = ViewModelProviders.of(this, viewmodelFactory).get(GoalDetailsViewModel::class.java)
        viewModel.setTaskCompleteStatus(taskId).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR)
                    Timber.e("Error in completing the task for task id %s and error is %s", taskId, it.message)
                else if (it.status == Status.SUCCESS) {
                    Timber.i("Successfully task completed")
                    taskBottomSheetDialog.dismiss()
                    //TODO : always get the new selected date
                    getTasksForSelectedDate(selectedDateInMills)
                }
            }
        })
    }

}

