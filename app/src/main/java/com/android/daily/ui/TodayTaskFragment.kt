package com.android.daily.ui


import android.animation.ObjectAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.daily.R
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.TodayTasksViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_today_task.*
import timber.log.Timber


class TodayTaskFragment : Fragment() {
    private val todayTasksViewModelFactory = InjectorUtils.provideTodayTaskViewModelFactory()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today_task, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //set tootlbar title
        getMainActivity()?.setToolBarTitle(getString(R.string.title_today))
        val progressAnimator = ObjectAnimator.ofInt(tasks_progress, "progress", 0,50)
        progressAnimator.duration = 900
        progressAnimator.start()
        getMainActivity()?.showBottomNavigationView()
        val viewmodel = ViewModelProviders.of(this, todayTasksViewModelFactory).get(TodayTasksViewModel::class.java)
        viewmodel.getUsename().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i(it.message)
                } else if (it.status == Status.SUCCESS) {
                    //successfully retrieved the user name ... now set it on textview
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


}
