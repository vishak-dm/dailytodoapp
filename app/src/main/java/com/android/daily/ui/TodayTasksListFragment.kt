package com.android.daily.ui


import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.daily.R
import com.android.daily.repository.model.TaskData
import com.android.daily.ui.adapters.TasksListAdapter
import com.android.daily.ui.adapters.TodayTasksListAdapter
import com.android.daily.viewModel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_my_goals_details.*
import kotlinx.android.synthetic.main.fragment_today_tasks_list.*
import java.util.*

class TodayTasksListFragment : Fragment() {
    lateinit var mView: View
    private var todayTasks: List<TaskData> = Collections.emptyList()
    private lateinit var viewModel: SharedViewModel
    private lateinit var taskAdapter: TodayTasksListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_today_tasks_list, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.setToolBarTitle("Today Tasks")
        configureRecyclerView()
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        viewModel.getTasksLiveData().observe(this, android.arch.lifecycle.Observer {
            todayTasks = it!!
            taskAdapter.setData(todayTasks)
        })

        get_mits_button.setOnClickListener {
            Snackbar.make(mView,taskAdapter.getSelectedTasks().size.toString(),Snackbar.LENGTH_SHORT).show()
        }

    }


    private fun configureRecyclerView() {
        taskAdapter = TodayTasksListAdapter(context!!, emptyList())
        val mLayoutManager = LinearLayoutManager(context)
        today_tasks_list_recycler_view.layoutManager = mLayoutManager
        today_tasks_list_recycler_view.itemAnimator = DefaultItemAnimator()
        today_tasks_list_recycler_view.adapter = taskAdapter
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


}
