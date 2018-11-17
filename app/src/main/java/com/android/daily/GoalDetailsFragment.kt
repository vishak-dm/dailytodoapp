package com.android.daily


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.daily.R.string.goal
import com.android.daily.repository.model.GoalsData
import com.android.daily.ui.MainActivity
import com.android.daily.utilities.extenstions.clearErrorOnTextChange
import kotlinx.android.synthetic.main.fragment_goal_details.*


class GoalDetailsFragment : Fragment() {
    private lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_goal_details, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.showBackButton()
        getMainActivity()?.setToolBarTitle(getString(R.string.goal_details))
        goalNametextInputLayout.clearErrorOnTextChange()
        goalDescriptionTextInputLayout.clearErrorOnTextChange()
        getMainActivity()?.getBackButton()?.setOnClickListener{
            findNavController().popBackStack()
        }
        goal_details_next_button.setOnClickListener {
            val goalName = goalNametextInputLayout.editText?.text.toString()
            val goalDescription = goalDescriptionTextInputLayout.editText?.text.toString()
            if (validateInput(goalName, goalDescription)) {
                val goal = GoalsData()
                goal.n = goalName
                goal.d = goalDescription
                val navigationDirections = GoalDetailsFragmentDirections.actionGoalDetailsFragmentToGoalTypeFragment(goal)
                findNavController().navigate(navigationDirections)
            }
        }
    }

    private fun validateInput(goalName: String, goalDescription: String): Boolean {
        if (goalName.isEmpty()) {
            goalNametextInputLayout.error = getString(R.string.empty_goal_name)
            return false
        }
        if (goalDescription.isEmpty()) {
            goalDescriptionTextInputLayout.error = getString(R.string.empty_goal_description)
            return false
        }
        return true
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


}
