package com.android.daily.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.ui.TaskDetailsFragmentArgs.fromBundle
import kotlinx.android.synthetic.main.fragment_task_details.*
import org.joda.time.Days
import android.os.Build
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.android.daily.ui.adapters.TaskTimeSessionsAdapter
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.TaskDetailsViewModel
import com.android.daily.vo.Status
import org.joda.time.DateTime
import org.joda.time.LocalDate
import timber.log.Timber


class TaskDetailsFragment : Fragment() {

    private lateinit var mView: View
    private lateinit var taskDetailsViewModel: TaskDetailsViewModel
    private var completeMenuItem: MenuItem? = null

    private val taskDetails by lazy {
        fromBundle(arguments).taskDetails
    }
    private lateinit var sessionAdapter: TaskTimeSessionsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_task_details, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initalizeUI()
        taskDetailsViewModel = ViewModelProviders.of(this, InjectorUtils.provideTaskDetailsModelFactory()).get(TaskDetailsViewModel::class.java)
    }

    private fun initalizeUI() {
        getMainActivity()?.showToolbar()
        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.setToolBarTitle(taskDetails.n.toUpperCase())
        task_description_tet_view.text = taskDetails.d.capitalize()

        var remainingDays = Days.daysBetween(LocalDate.now(), LocalDate(taskDetails.dd)).days
        if (remainingDays < 0)
            remainingDays = 0
        task_remaining_days_text_view.text = String.format(getString(R.string.days_left), remainingDays)
        add_session_button.setOnClickListener {
            val navDirections = TaskDetailsFragmentDirections.actionTaskDetailsFragmentToTaskTimerFragment(taskDetails)
            findNavController().navigate(navDirections)
        }
        if (taskDetails.c || isTaskExpired()) {
            remaining_days_group.visibility = View.GONE
            add_session_button.visibility = View.GONE
        } else {
            remaining_days_group.visibility = View.VISIBLE
            add_session_button.visibility = View.VISIBLE
        }

        configureRecyclerView()

    }

    private fun configureRecyclerView() {
        sessionAdapter = TaskTimeSessionsAdapter(taskDetails.sl)
        val mLayoutManager = LinearLayoutManager(context)
        session_recycler_view.layoutManager = mLayoutManager
        session_recycler_view.itemAnimator = DefaultItemAnimator()
        session_recycler_view.adapter = sessionAdapter

    }

    private fun isTaskExpired(): Boolean {
        var remainingDays = Days.daysBetween(DateTime.now(), DateTime(taskDetails.dd)).days
        return remainingDays < 0
    }

    private fun showConfirmationDialog() {
        val builder: AlertDialog.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = AlertDialog.Builder(context!!, android.R.style.Theme_Material_Dialog_Alert)
        } else {
            builder = AlertDialog.Builder(context!!)
        }
        builder.setTitle(getString(R.string.complete_task))
                .setMessage(R.string.complete_task_user_prompt)
                .setPositiveButton(android.R.string.yes) { dialog, which ->
                    completeTask()
                }
                .setNegativeButton(android.R.string.no) { dialog, which ->
                    // do nothing
                    completeMenuItem?.isVisible = true
                    remaining_days_group.visibility = View.VISIBLE
                }
                .show()
    }

    private fun completeTask() {
        taskDetailsViewModel.setTaskCompleteStatus(taskDetails.id).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i("Error while completing task %s", it.message)
                    completeMenuItem?.isVisible = true
                    remaining_days_group.visibility = View.VISIBLE
                    add_session_button.visibility = View.VISIBLE

                } else if (it.status == Status.SUCCESS) {
                    completeMenuItem?.isVisible = false
                    remaining_days_group.visibility = View.GONE
                    add_session_button.visibility = View.GONE
                }
            }
        })

    }

    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.task_details_menu, menu)
        completeMenuItem = menu?.findItem(R.id.task_completed)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        completeMenuItem = menu?.findItem(R.id.task_completed)
        completeMenuItem?.isVisible = !(taskDetails.c || isTaskExpired())
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.task_completed) {
            showConfirmationDialog()
        }
        return true
    }


}
