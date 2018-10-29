package com.android.daily.ui


import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
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
import com.android.daily.ui.adapters.MitListAdapter
import com.android.daily.utilities.CommonUtils
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.SharedViewModel
import com.android.daily.viewModel.TodayTasksViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_today_task.*
import timber.log.Timber
import org.joda.time.format.PeriodFormatterBuilder
import org.joda.time.PeriodType
import org.joda.time.DateTime
import org.joda.time.Period
import java.util.*
import kotlin.collections.ArrayList

import com.android.daily.ui.adapters.MitClickListener


class TodayTaskFragment : Fragment() {
    private val todayTasksViewModelFactory = InjectorUtils.provideTodayTaskViewModelFactory()
    private lateinit var mView: View
    private lateinit var todayTasksViewModel: TodayTasksViewModel
    private var todayTasksList: List<TaskData> = Collections.emptyList()
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var mitTaskAdapter: MitListAdapter
    private val taskClickListener: MitClickListener = this::onMitClicked
    private val mitList = ArrayList<TaskData>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_today_task, container, false)
        return mView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set tootlbar title
        configureRecyclerView()
        setRemainingHours()
        getMainActivity()?.showBottomNavigationView()
        getMainActivity()?.hideCompletedText()
        todayTasksViewModel = ViewModelProviders.of(this, todayTasksViewModelFactory).get(TodayTasksViewModel::class.java)
        setUserData()
        getTodayTasks()
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid activity")
        add_mit_button.setOnClickListener {
            //go to tasks list view
            sharedViewModel.setTasksList(todayTasksList)
            findNavController().navigate(R.id.action_todayTaskFragment_to_todayTasksListFragment)
        }

    }

    private fun getTodayTasks() {
        todayTasksViewModel.getTodayTasks(DateTime.now().withTimeAtStartOfDay().millis, DateTime.now().plusDays(1).withTimeAtStartOfDay().millis).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i(it.message)
                } else if (it.status == Status.SUCCESS) {
                    if (it.data != null) {
                        todayTasksList = it.data
                        CommonUtils.animateTextView(0, todayTasksList.size, today_number_of_tasks_textview)
                        setTodayMitData()
                    }
                }
                setMitCardVisibility()
                CommonUtils.applyLayoutAnimations(today_task_constraintlayout)
            }
        })

    }

    private fun setMitCardVisibility() {
        if (mitList.size > 0) {
            mit_cardview.visibility = View.VISIBLE
            mit_recyler_view.visibility = View.VISIBLE
            mit_tutorial.visibility = View.GONE
            mitTaskAdapter.setData(mitList)
            CommonUtils.animateTextView(0, mitList.size, today_number_of_mit_textview)
        } else {
            mit_cardview.visibility = View.VISIBLE
            mit_tutorial.visibility = View.VISIBLE
            mit_recyler_view.visibility = View.GONE
        }


    }

    private fun setTodayMitData() {
        mitList.clear()
        if (todayTasksList.isNotEmpty()) {
            for (task in todayTasksList) {
                if (task.mit)
                    mitList.add(task)
            }
        }
    }

    private fun setUserData() {
        todayTasksViewModel.getUsername().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i(it.message)
                } else if (it.status == Status.SUCCESS) {
                    //successfully retrieved the user name
                    getMainActivity()?.setToolBarTitle(getString(R.string.welcome)+" "+it.data?.capitalize())
                }
            }
        })

    }

    private fun configureRecyclerView() {
        mitTaskAdapter = MitListAdapter(context!!, Collections.emptyList(), taskClickListener)
        val mLayoutManager = LinearLayoutManager(context)
        mit_recyler_view.layoutManager = mLayoutManager
        mit_recyler_view.itemAnimator = DefaultItemAnimator()
        mit_recyler_view.adapter = mitTaskAdapter
    }

    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }

    private fun setRemainingHours() {
        val startDate = DateTime.now()
        val endDate = startDate.plusDays(1).withTimeAtStartOfDay()

        val period = Period(startDate, endDate, PeriodType.dayTime())
        tasks_progress.max = 24
        val progressAnimator = ObjectAnimator.ofInt(tasks_progress, "progress", 0, 24 - period.hours)
        progressAnimator.duration = 1300
        progressAnimator.start()

        val formatter = PeriodFormatterBuilder()
                .appendHours()
                .toFormatter()
        hrstextview.text = formatter.print(period)
    }


    fun onMitClicked(task: TaskData) {
        val navDirections = TodayTaskFragmentDirections.actionTodayTaskFragmentToTaskDetailsFragment(task)
        findNavController().navigate(navDirections)
    }
}


