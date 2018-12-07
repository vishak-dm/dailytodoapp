package com.android.daily.ui


import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.android.daily.R
import com.android.daily.repository.model.SessionsData
import com.android.daily.ui.TaskSessionFragmentArgs.fromBundle
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.TaskTimerViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_task_session.*
import org.joda.time.DateTime
import timber.log.Timber


class TaskSessionFragment : Fragment() {
    private lateinit var mView: View
    private val taskDetails by lazy {
        fromBundle(arguments).taskDetails
    }
    private val duration by lazy {
        fromBundle(arguments).duration
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mView = inflater.inflate(R.layout.fragment_task_session, container, false)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMainActivity()?.showToolbar()
        getMainActivity()?.showBackButton()
        getMainActivity()?.hideBottomNavigationView()
        getMainActivity()?.setToolBarTitle(getString(R.string.add_session))
        add_session_button.setOnClickListener {
            saveSession()
        }
        setSessionDurationText()
    }

    private fun setSessionDurationText() {
        val minutesUntilFinished = duration / 60
        val secondsInMinuteUntilFinished = duration - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        session_duration_text_view.text = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"

    }

    private fun saveSession() {
        val sessionName = session_name_text_input_layout.editText?.text.toString()
        val sessionDescription = session_description_text_input_layout.editText?.text.toString()
        if (sessionName.isEmpty()) {
            session_name_text_input_layout.error = getString(R.string.invalid_session_name)
            return
        }
        if (sessionDescription.isEmpty()) {
            session_description_text_input_layout.error = getString(R.string.invalid_session_description)
            return
        }
        //TODO:we need to get the session started time dynamically.. cna change in the version 2
        val sessionStartedTime = DateTime.now().millis
        val sessionsData = SessionsData(sessionName, sessionDescription, sessionStartedTime, duration)
        val sessions = ArrayList<SessionsData>()
        sessions.addAll(taskDetails.sl)
        sessions.add(sessionsData)
        taskDetails.sl = sessions
        add_session_progressbar.visibility = View.VISIBLE
        add_session_button.visibility = View.GONE
        //TODO:actually we need to read new task data and then update in a transaction so as to avoid concurrency problems... but no time so just updating
        val viewModel = ViewModelProviders.of(this, InjectorUtils.provideTaskTimerViewModelFactory()).get(TaskTimerViewModel::class.java)
        viewModel.updateTaskSessionTime(taskDetails).observe(viewLifecycleOwner, android.arch.lifecycle.Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.i("Error in updating task timer info  %s", it.message)
                    Snackbar.make(mView, it.message.toString(), Snackbar.LENGTH_SHORT).show()
                } else if (it.status == Status.SUCCESS) {
                    Timber.i("Success  in updating timer  task")
                    //just pop from the stack
                    findNavController().popBackStack(R.id.taskTimerFragment, true)
                }
                add_session_progressbar.visibility = View.GONE
                add_session_button.visibility = View.VISIBLE

            }
        })

    }

    override fun onDetach() {
        super.onDetach()
        getMainActivity()?.hideBackButton()
    }

    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }


}
