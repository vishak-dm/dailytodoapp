package com.android.daily.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.ui.GoalTypeFragmentArgs.fromBundle
import kotlinx.android.synthetic.main.fragment_goal_type.*


class GoalTypeFragment : Fragment() {
    private lateinit var mView: View
    private val goal by lazy {
        fromBundle(arguments).goaldetails
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_goal_type, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.showBackButton()
        getMainActivity()?.setToolBarTitle(getString(R.string.choose_goal_type))
        getMainActivity()?.getBackButton()?.setOnClickListener {
            findNavController().popBackStack()
        }
        goal_type_next_button.setOnClickListener {
            goal.gt = getGoalType()
            goal.gc = getGoalCategory()
            val navigationDirections = GoalTypeFragmentDirections.actionGoalTypeFragmentToGoalDateFragment(goal)
            findNavController().navigate(navigationDirections)
        }
    }

    private fun getGoalCategory(): Int {
        when (radioGroup.checkedRadioButtonId) {
            short_term_radio_button.id -> return 0
            long_term_radio_button.id -> return 1
        }
        return -1
    }

    private fun getGoalType(): Int {
        when (goal_category_radio_group.checkedRadioButtonId) {
            personal_goal_radio_button.id -> return 1
            career_radio_button.id -> return 2
            finance_radio_button.id -> return 3
        }
        return -1
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


}
