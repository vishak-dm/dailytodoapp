package com.android.daily.ui


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.repository.model.TaskData
import com.android.daily.ui.adapters.ChooseMitAdapter
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.AddMitViewModel
import com.android.daily.viewModel.SharedViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_today_task.*
import kotlinx.android.synthetic.main.fragment_today_tasks_list.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

class TodayTasksListFragment : Fragment() {
    lateinit var mView: View
    private var todayTasks: List<TaskData> = Collections.emptyList()
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var taskAdapter: ChooseMitAdapter
    private lateinit var addMitViewModel: AddMitViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_today_tasks_list, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.setToolBarTitle(getString(R.string.choose_mit))
        configureRecyclerView()
        addMitViewModel = ViewModelProviders.of(this, InjectorUtils.provideAddMitViewModelFactory()).get(AddMitViewModel::class.java)
        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        } ?: throw Exception("Invalid activity")

        sharedViewModel.getTasksLiveData().observe(this, android.arch.lifecycle.Observer {
            todayTasks = it!!
            //filter out the oly m false data
            setListOfSelectableMits()
        })


        get_mits_button.setOnClickListener {
            get_mits_button.visibility = View.GONE
            add_mit_task_progressbar.visibility = View.VISIBLE
            addMitViewModel.addMitTasks(taskAdapter.getSelectedMits()).observe(viewLifecycleOwner, android.arch.lifecycle.Observer {
                if (it != null) {
                    if (it.status == Status.ERROR) {
                        Timber.i("Error while adding m tasks %s", it.message)
                        Snackbar.make(mView, R.string.error_adding_mit, Snackbar.LENGTH_SHORT).show()
                    } else if (it.status == Status.SUCCESS) {
                        Timber.i("Successfully added m task")
                    }
                    get_mits_button.visibility = View.VISIBLE
                    add_mit_task_progressbar.visibility = View.GONE
                    findNavController().popBackStack()
                }
            })
        }

    }

    private fun setListOfSelectableMits() {
        val potentialMits = ArrayList<TaskData>()
        if (todayTasks.isNotEmpty()) {
            for (task in todayTasks) {
                if (!task.m && !task.c)
                    potentialMits.add(task)
            }

            if (potentialMits.isEmpty()) {
                get_mits_button.visibility = View.GONE
                empty_mit_state.visibility = View.VISIBLE
                add_mit_task_progressbar.visibility = View.GONE

            } else {
                get_mits_button.visibility = View.VISIBLE
                add_mit_task_progressbar.visibility = View.VISIBLE
                empty_mit_state.visibility = View.GONE
                taskAdapter.setData(potentialMits)

            }

        }

    }


    private fun configureRecyclerView() {
        taskAdapter = ChooseMitAdapter(context!!, emptyList())
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
