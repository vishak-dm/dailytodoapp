package com.android.daily


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.android.daily.ui.MainActivity
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.AddGoalViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_goal_date.*
import org.joda.time.DateTime
import timber.log.Timber
import java.util.*


class GoalDateFragment : Fragment() {
    private lateinit var mView: View
    private var selectedDateInMills = 0L
    private val goal by lazy {
        GoalTypeFragmentArgs.fromBundle(arguments).goaldetails
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_goal_date, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goals_dates_view.dateTextAppearance = R.style.fontBookAppearance
        goals_dates_view.weekDayTextAppearance = R.style.fontBookAppearance
        getMainActivity()?.setToolBarTitle(getString(R.string.choose_target_date))
        getMainActivity()?.showBackButton()
        goals_dates_view.minDate = System.currentTimeMillis()
        getMainActivity()?.getBackButton()?.setOnClickListener {
            findNavController().popBackStack()
        }
        goals_dates_view.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedDateInMills = calendar.timeInMillis
        }

        goal_date_next_button.setOnClickListener {

            if (selectedDateInMills == 0L) {
                Snackbar.make(mView, R.string.empty_due_date, Snackbar.LENGTH_SHORT).show()
            } else {
                add_goal_progressbar.visibility = View.VISIBLE
                goal_date_next_button.visibility = View.GONE
                goal.dd = selectedDateInMills
                val viewModel = ViewModelProviders.of(this, InjectorUtils.provideAddGoalsViewModelFactory()).get(AddGoalViewModel::class.java)
                viewModel.saveGoal(goal).observe(viewLifecycleOwner, android.arch.lifecycle.Observer {
                    if (it != null) {
                        if (it.status == Status.ERROR) {
                            Timber.i("Error while adding the goals to database %s", it.message)
                            Snackbar.make(mView, getString(R.string.error_adding_goal), Snackbar.LENGTH_SHORT).show()
                        } else if (it.status == Status.SUCCESS) {
                            Timber.i("Successfully added the goals to database")
                            Navigation.findNavController(mView).popBackStack(R.id.goalDetailsFragment, true)
                        }
                        add_goal_progressbar.visibility = View.GONE
                        goal_date_next_button.visibility = View.VISIBLE
                    }
                })
            }
        }
    }

    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


}
