package com.android.daily.ui


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.android.daily.R
import com.android.daily.ui.adapters.MyGoalsAdapter
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.MyGoalsViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_my_projects.*
import timber.log.Timber
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.android.daily.repository.model.GoalsData
import com.android.daily.ui.adapters.GoalClickListener
import com.android.daily.utilities.CommonUtils
import com.android.daily.utilities.views.GridSpacingItemDecoration
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class MyGoalsFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory:ViewModelProvider.Factory

    private lateinit var mView: View
    private lateinit var goalsAdapter: MyGoalsAdapter
    private val goalClickListener: GoalClickListener = this::onGoalClicked

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_projects, container, false)
        getMainActivity()?.setToolBarTitle(getString(R.string.goals))
        getMainActivity()?.showBottomNavigationView()
        getMainActivity()?.showToolbar()
        getMainActivity()?.hideBackButton()
        goalsAdapter = MyGoalsAdapter(context!!, emptyList(), goalClickListener)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        goals_recycler_view.layoutManager = mLayoutManager
        goals_recycler_view.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        goals_recycler_view.adapter = goalsAdapter
        add_project_floating_button.setOnClickListener {
            Navigation.findNavController(mView).navigate(R.id.action_myProjectsFragment_to_goalDetailsFragment)
        }
        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(MyGoalsViewModel::class.java)
        my_goals_progressbar.visibility = View.VISIBLE
        viewModel.getMyGoals().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i("Error in retriving the goals %s", it.message)
                    Snackbar.make(mView, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                } else if (it.status == Status.SUCCESS) {
                    Timber.i("Successfully retrieved the goals")
                    it.data?.let { it1 ->
                        goalsAdapter.setData(it1)
                    }
                }
            }
            my_goals_progressbar.visibility = View.GONE
        })
    }


    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


    fun onGoalClicked(goal: GoalsData) {
        val navigationDirections = MyGoalsFragmentDirections.actionMyProjectsFragmentToMyGoalsDetailsFragment(goal)
        view?.let {
            findNavController().navigate(navigationDirections)
        }
    }


}

