package com.android.daily.ui


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.repository.model.TaskData
import com.android.daily.ui.adapters.CalendarTasksAdapter
import com.android.daily.ui.adapters.TaskClickListener
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.MyCalendarViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_my_calendar.*
import org.joda.time.DateTime
import timber.log.Timber
import java.util.*


class MyCalendarFragment : Fragment() {
    private lateinit var mView: View
    private var selectedDateInMills: Long = 0
    private lateinit var myCalendarViewModel: MyCalendarViewModel
    private var tasksList: MutableList<TaskData> = mutableListOf()
    private lateinit var calendarTaskAdapter: CalendarTasksAdapter
    private val taskClickListener: TaskClickListener = this::onTaskClicked


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
        myCalendarViewModel = ViewModelProviders.of(this, InjectorUtils.provideMyCalendarViewModelFactory()).get(MyCalendarViewModel::class.java)
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
                .observe(viewLifecycleOwner, android.arch.lifecycle.Observer {
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

    fun onTaskClicked(task: TaskData) {
        val navDirections = MyCalendarFragmentDirections.actionMyCalendarFragmentToTaskDetailsFragment(task)
        findNavController().navigate(navDirections)
    }


}

