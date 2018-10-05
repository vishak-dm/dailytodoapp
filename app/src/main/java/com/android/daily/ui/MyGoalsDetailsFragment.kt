package com.android.daily.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.daily.R
import com.android.daily.ui.MyGoalsDetailsFragmentArgs.fromBundle
import kotlinx.android.synthetic.main.fragment_my_goals_details.*
import android.animation.ValueAnimator
import android.widget.TextView
import androidx.navigation.fragment.findNavController


class MyGoalsDetailsFragment : Fragment() {
    private val goal by lazy {
        fromBundle(arguments).goalId
    }

    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_goals_details, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.hideBottomNavigationView()
        setTasksDetails()
        goal_name_details_text_view.text = goal.goalName
        goal_description_details_text_view.text = goal.goalDescription
        add_task_details_button.setOnClickListener {
            findNavController().navigate(R.id.action_myGoalsDetailsFragment_to_addTaskFragment)
        }

    }

    private fun setTasksDetails() {
        animateTextView(0, 30, task_completed_number_details_text_view)
        animateTextView(0, 50, total_tasks_details_text_view)
        animateTextView(0, 4, no_days_details_text_view)
    }


    fun animateTextView(initialValue: Int, finalValue: Int, textview: TextView) {

        val valueAnimator = ValueAnimator.ofInt(initialValue, finalValue)
        valueAnimator.duration = 1500

        valueAnimator.addUpdateListener { valueAnimator -> textview.text = valueAnimator.animatedValue.toString() }
        valueAnimator.start()
    }


    private fun getMainActivity(): MainActivity? {
        if (activity is MainActivity)
            return activity as MainActivity
        else
            return null
    }


}
