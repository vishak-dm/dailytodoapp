package com.android.daily.ui


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.daily.R
import kotlinx.android.synthetic.main.fragment_my_projects.*

class MyProjectsFragment : Fragment() {
    private lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_projects, container, false)
        getMainActivity()?.setToolBarTitle(getString(R.string.goals))
        getMainActivity()?.showBottomNavigationView()
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_project_floating_button.setOnClickListener{
            Navigation.findNavController(mView).navigate(R.id.action_myProjectsFragment_to_addProjectFragment)
        }
    }


    private fun getMainActivity(): MainActivity? {
        if (activity is MainActivity)
            return activity as MainActivity
        else
            return null
    }



}
