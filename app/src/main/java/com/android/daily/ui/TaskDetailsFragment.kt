package com.android.daily.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.ui.TaskDetailsFragmentArgs.fromBundle
import com.android.daily.utilities.CommonUtils.Companion.animateTextView
import kotlinx.android.synthetic.main.fragment_task_details.*
import org.joda.time.Days
import android.os.Build
import android.support.v7.app.AlertDialog
import android.view.*
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
        getMainActivity()?.setToolBarTitle(taskDetails.n.capitalize())
        task_description_tet_view.text = taskDetails.d.capitalize()

        var remainingDays = Days.daysBetween(LocalDate.now(), LocalDate(taskDetails.dd)).days
        if (remainingDays < 0)
            remainingDays = 0
        animateTextView(0, remainingDays, task_remaining_days_text_view)
        start_pomarado_timer_button.setOnClickListener {
            val navDirections = TaskDetailsFragmentDirections.actionTaskDetailsFragmentToTaskTimerFragment(taskDetails)
            findNavController().navigate(navDirections)
        }
        if (taskDetails.c || isTaskExpired()) {
            complete_task_button.visibility = View.GONE
            remaining_days_group.visibility = View.GONE
            start_timer_constraint_layout.visibility = View.GONE
        } else {
            complete_task_button.visibility = View.VISIBLE
            remaining_days_group.visibility = View.VISIBLE
            start_timer_constraint_layout.visibility = View.VISIBLE

        }
        complete_task_button.setOnClickListener {
            showConfirmationDialog()
        }


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
                    complete_task_button.visibility = View.VISIBLE
                    complete_progress_bar.visibility = View.GONE
                    remaining_days_group.visibility = View.VISIBLE
                }
                .show()
    }

    private fun completeTask() {
        complete_task_button.visibility = View.GONE
        complete_progress_bar.visibility = View.VISIBLE
        taskDetailsViewModel.setTaskCompleteStatus(taskDetails.id).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i("Error while completing task %s", it.message)
                    complete_task_button.visibility = View.VISIBLE
                    complete_progress_bar.visibility = View.GONE
                    completeMenuItem?.isVisible = false
                    remaining_days_group.visibility = View.VISIBLE
                    start_timer_constraint_layout.visibility = View.VISIBLE

                } else if (it.status == Status.SUCCESS) {
                    complete_task_button.visibility = View.GONE
                    complete_progress_bar.visibility = View.GONE
                    remaining_days_group.visibility = View.GONE
                    start_timer_constraint_layout.visibility = View.GONE
                    completeMenuItem?.isVisible = true
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
        completeMenuItem?.isVisible = taskDetails.c || isTaskExpired()
        super.onPrepareOptionsMenu(menu)
    }


}
