package com.android.daily.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.daily.R
import com.android.daily.ui.adapters.MyGoalsAdapter
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.MyGoalsViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_my_projects.*
import timber.log.Timber
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import androidx.navigation.fragment.findNavController
import com.android.daily.repository.model.GoalsData
import com.android.daily.ui.adapters.ClickListener
import com.android.daily.utilities.CommonUtils
import com.android.daily.utilities.views.GridSpacingItemDecoration


class MyGoalsFragment : Fragment() {
    private lateinit var mView: View
    private lateinit var goalsAdapter: MyGoalsAdapter
    private val clickListener: ClickListener = this::onGoalClicked

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_my_projects, container, false)
        getMainActivity()?.setToolBarTitle(getString(R.string.goals))
        getMainActivity()?.showBottomNavigationView()

        goalsAdapter = MyGoalsAdapter(context!!, emptyList(), clickListener)

        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mLayoutManager = GridLayoutManager(context, 2)
        goals_recycler_view.layoutManager = mLayoutManager
        goals_recycler_view.addItemDecoration(GridSpacingItemDecoration(2, CommonUtils.dpToPx(10, resources), true))
        goals_recycler_view.itemAnimator = DefaultItemAnimator()
        goals_recycler_view.adapter = goalsAdapter
        add_project_floating_button.setOnClickListener {
            Navigation.findNavController(mView).navigate(R.id.action_myProjectsFragment_to_addProjectFragment)
        }
        val viewModel = ViewModelProviders.of(this, InjectorUtils.provideMyGoalsViewModelFactory()).get(MyGoalsViewModel::class.java)
        my_goals_progressbar.visibility = View.VISIBLE
        viewModel.getMyGoals().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i("Error in retriving the goals %s", it.message)
                    Snackbar.make(mView, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                } else if (it.status == Status.SUCCESS) {
                    Timber.i("Successfully retrieved the goals")
                    it.data?.let { it1 -> goalsAdapter.setData(it1) }
                }
                my_goals_progressbar.visibility = View.GONE
            }
        })
    }


    private fun getMainActivity(): MainActivity? {
        if (activity is MainActivity)
            return activity as MainActivity
        else
            return null
    }


    fun onGoalClicked(goal: GoalsData) {
        val navigationDirections = MyGoalsFragmentDirections.actionMyProjectsFragmentToMyGoalsDetailsFragment(goal)
        view?.let {
            findNavController().navigate(navigationDirections)
        }
    }


}

