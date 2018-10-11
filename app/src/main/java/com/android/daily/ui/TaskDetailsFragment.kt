package com.android.daily.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.daily.R
import com.android.daily.ui.TaskDetailsFragmentArgs.fromBundle
import com.android.daily.utilities.CommonUtils.Companion.animateTextView
import kotlinx.android.synthetic.main.fragment_task_details.*
import org.joda.time.Days
import org.joda.time.LocalDate


class TaskDetailsFragment : Fragment() {

    private lateinit var mView: View

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
        getMainActivity()?.hideToolbar()
        task_name_text_view.text = taskDetails.taskName.capitalize()
        task_description_tet_view.text = taskDetails.taskDescription.capitalize()
        var remainingDays = Days.daysBetween(LocalDate.now(), LocalDate(taskDetails.taskDueDate)).days
        if (remainingDays < 0)
            remainingDays = 0
        animateTextView(0, remainingDays, task_remaining_days_text_view)
    }

    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


}
