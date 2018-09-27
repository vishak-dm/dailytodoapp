package com.android.daily


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.daily.ui.MainActivity
import com.android.daily.utilities.InjectorUtils
import com.android.daily.utilities.extenstions.clearErrorOnTextChange
import com.android.daily.viewModel.AuthenticationViewModel
import com.android.daily.vo.Status
import kotlinx.android.synthetic.main.fragment_user_details.*
import timber.log.Timber

class UserDetailsFragement : Fragment() {
    lateinit var mView: View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_user_details, container, false)
        hideBottomNavigation()
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        name_text_input_layout.clearErrorOnTextChange()
        done_button.setOnClickListener {
            val name = name_text_input_layout.editText?.text.toString()
            if (name.isEmpty())
                name_text_input_layout.error = getString(R.string.invalid_name)
            else
                setUserDetails(name)
        }

    }

    private fun setUserDetails(name: String) {
        //set the name of the user in the database
        user_details_progressbar.visibility = View.VISIBLE
        done_button.visibility = View.GONE
        val viewModel = ViewModelProviders.of(this, InjectorUtils.provideAuthenticationViewModelFactory()).get(AuthenticationViewModel::class.java)
        viewModel.saveUserDetails(name).observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.status == Status.ERROR) {
                    Timber.e("Error in adding the name %s", it.message)
                    Snackbar.make(mView, it.message.toString(), Snackbar.LENGTH_LONG).show()
                } else if (it.status == Status.SUCCESS) {
                    Timber.i("Adde user name successfully")
                    //go to main screen
                    Navigation.findNavController(mView).navigate(R.id.action_userDetailsFragement_to_mainFragment)
                }
                user_details_progressbar.visibility = View.GONE
                done_button.visibility = View.VISIBLE
            }
        })
    }

    private fun hideBottomNavigation() {
        if (activity is MainActivity) {
            Timber.i("Hiding bottom navigation view in register fragment")
            val mainActivity = activity as MainActivity
            mainActivity.hideBottomNavigationView()
        }
    }


}
