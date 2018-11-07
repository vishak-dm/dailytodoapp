package com.android.daily


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.daily.ui.MainActivity
import kotlinx.android.synthetic.main.fragment_goal_details.*


class GoalDetailsFragment : Fragment() {
    private lateinit var mView: View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_goal_details, container, false)
        return  mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.hideBottomNavigationView()
        goal_details_next_button.setOnClickListener { findNavController().navigate(R.id.action_goalDetailsFragment_to_goalDateFragment) }
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


}
