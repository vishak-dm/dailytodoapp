package com.android.daily.ui


import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.daily.R
import com.android.daily.repository.model.TaskData
import com.android.daily.utilities.CommonUtils
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.TodayTasksViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_today_task.*
import timber.log.Timber
import org.joda.time.format.PeriodFormatterBuilder
import org.joda.time.PeriodType
import org.joda.time.DateTime
import org.joda.time.Period


class TodayTaskFragment : Fragment() {
    private val todayTasksViewModelFactory = InjectorUtils.provideTodayTaskViewModelFactory()
    private lateinit var mView: View
    private lateinit var todayTasksViewModel: TodayTasksViewModel
    private lateinit var todayTasksList: List<TaskData>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_today_task, container, false)
        return mView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set tootlbar title
        getMainActivity()?.setToolBarTitle(getString(R.string.title_today))
        setRemainingHours()
        getMainActivity()?.showBottomNavigationView()
        todayTasksViewModel = ViewModelProviders.of(this, todayTasksViewModelFactory).get(TodayTasksViewModel::class.java)
        setUserData()
        getTodayTasks()
    }

    private fun getTodayTasks() {
        todayTasksViewModel.getTodayTasks(DateTime.now().plusDays(1).withTimeAtStartOfDay().millis).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i(it.message)
                } else if (it.status == Status.SUCCESS) {
                    if (it.data != null) {
                        todayTasksList = it.data
                        CommonUtils.animateTextView(0, todayTasksList.size, today_number_of_tasks_textview)
                    }
                }
            }
        })

    }

    private fun setUserData() {
        todayTasksViewModel.getUsename().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i(it.message)
                } else if (it.status == Status.SUCCESS) {
                    //successfully retrieved the user name
                    welcome_text_view.text = getString(R.string.welcome)
                    user_name_text_view.text = it.data?.capitalize()
                }
            }
        })

    }

    private fun getMainActivity(): MainActivity? {
        if (activity is MainActivity)
            return activity as MainActivity
        else
            return null
    }

    private fun setRemainingHours() {
        val startDate = DateTime.now()
        val endDate = startDate.plusDays(1).withTimeAtStartOfDay()

        val period = Period(startDate, endDate, PeriodType.dayTime())
        tasks_progress.max = 24
        val progressAnimator = ObjectAnimator.ofInt(tasks_progress, "progress", 0, 24 - period.hours)
        progressAnimator.duration = 900
        progressAnimator.start()

        val formatter = PeriodFormatterBuilder()
                .appendHours()
                .toFormatter()
        hrstextview.text = formatter.print(period)
    }


}
