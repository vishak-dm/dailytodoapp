package com.android.daily


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.daily.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_goal_date.*
import org.threeten.bp.LocalDate


class GoalDateFragment : Fragment() {
    private lateinit var mView : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_goal_date, container, false)
        return  mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goals_dates_view.state().edit().setMinimumDate(LocalDate.now()).commit()
        goals_dates_view.setDateTextAppearance(R.style.fontBookAppearance)
        goals_dates_view.setHeaderTextAppearance(R.style.headingTextApperance)
        goals_dates_view.setWeekDayTextAppearance(R.style.fontBookAppearance)
        getMainActivity()?.showBackButton()
        goal_date_next_button.setOnClickListener { findNavController().navigate(R.id.action_goalDateFragment_to_goalTypeFragment) }
    }

    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


}
