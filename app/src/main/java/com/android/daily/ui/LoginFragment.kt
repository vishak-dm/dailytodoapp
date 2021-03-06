package com.android.daily.ui


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.android.daily.R
import com.android.daily.utilities.InjectorUtils
import com.android.daily.utilities.extenstions.clearErrorOnTextChange
import com.android.daily.viewModel.AuthenticationViewModel
import com.android.daily.vo.Status
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_login.*
import timber.log.Timber
import javax.inject.Inject

class LoginFragment : DaggerFragment() {
    //injecting the viewmodel factory
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private var mView: View? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_login, container, false)
        hideBottomNavigation()
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("LoginFragment")
        setTextWatchers()
        getMainActivity()?.setToolBarTitle("")
        getMainActivity()?.hideBackButton()
        login_button.setOnClickListener { loginUserWithEmailAndPassword() }
        signup_text_view.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment))
    }


    private fun loginUserWithEmailAndPassword() {
        val email = email_text_input_layout.editText?.text.toString()
        val password = login_password_text_input_layout.editText?.text.toString()
        if (validateInput(email, password)) {
            login_progress.visibility = View.VISIBLE
            login_button.visibility = View.INVISIBLE
            val viewModel = ViewModelProviders.of(this, viewModelFactory).get(AuthenticationViewModel::class.java)
            viewModel.loginUserWithEmailAndPassword(email, password).observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    if (it.status == Status.ERROR) {
                        Timber.e("Error sign in of user using google api  ${it.message}")
                        Snackbar.make(mView!!, it.message.toString(), Snackbar.LENGTH_LONG).show()
                    } else if (it.status == Status.SUCCESS) {
                        //navigate to the main screen
                        //we need to pop the old fragment because we come to main fragment from 3 fragments i.e login , register and splash which shud not be seen again
                        //since we are moving to a differnet graph we need to pop ourselves
                        // Timber.i("User logged in successfully popping login screen ${Navigation.findNavController(mView!!).popBackStack()}")
                        Navigation.findNavController(mView!!).navigate(R.id.action_loginFragment_to_mainFragment)
                    }
                }
                login_progress.visibility = View.GONE
                login_button.visibility = View.VISIBLE
            })

        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Timber.e("Invalid email id")
            email_text_input_layout.error = getString(R.string.error_invalid_email)
            return false
        }

        if (password.isEmpty()) {
            Timber.e("Invalid password id")
            login_password_text_input_layout.error = getString(R.string.empty_password)
            return false
        }

        return true
    }

    private fun setTextWatchers() {
        email_text_input_layout.clearErrorOnTextChange()
        login_password_text_input_layout.clearErrorOnTextChange()
    }

    private fun hideBottomNavigation() {
        getMainActivity()?.hideBottomNavigationView()
    }

    private fun getMainActivity(): MainActivity? {
        return if (activity is MainActivity)
            activity as MainActivity
        else
            null
    }

}
