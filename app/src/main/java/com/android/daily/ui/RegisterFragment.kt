package com.android.daily.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.daily.R
import com.android.daily.utilities.InjectorUtils
import kotlinx.android.synthetic.main.fragment_register.*
import com.android.daily.utilities.extenstions.clearErrorOnTextChange
import com.android.daily.viewModel.AuthenticationViewModel
import com.android.daily.vo.Status
import timber.log.Timber

class RegisterFragment : Fragment() {
    private var mView: View? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_register, container, false)
        hideBottomNavigation()
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.tag("RegisterFragment")
        action_login_text_view.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_registerFragment_to_loginFragment))
        setTextWatchers()
        signup_button.setOnClickListener {
            createUser()
        }
    }

    private fun createUser() {
        val email = register_email_text_input.editText?.text.toString()
        val password = register_password_text_input.editText?.text.toString()
        val confirmPassword = register_password_confirm_text_input.editText?.text.toString()
        if (validateInput(email, password, confirmPassword)) {
            signup_button.visibility = View.INVISIBLE
            signup_progress.visibility = View.VISIBLE
            val viewmodel = ViewModelProviders.of(this, InjectorUtils.provideAuthenticationViewModelFactory()).get(AuthenticationViewModel::class.java)
            viewmodel.createUser(email, password).observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    if (it.status == Status.ERROR) {
                        Timber.e("Error in creating the user ${it.message.toString()}")
                        Snackbar.make(mView!!, it.message.toString(), Snackbar.LENGTH_LONG).show()
                    } else if (it.status == Status.SUCCESS) {
                        Timber.i("User created successfully")
                        //navigate to the main screen
                        //we need to pop the before fragment because we come to main fragment from 3 fragments i.e login , register and splash which shud not be seen again
                        Navigation.findNavController(mView!!).navigate(R.id.action_registerFragment_to_userDetailsFragement)

                    }
                }
                signup_progress.visibility = View.GONE
                signup_button.visibility = View.VISIBLE
            })

        }

    }

    private fun setTextWatchers() {
        register_email_text_input.clearErrorOnTextChange()
        register_password_text_input.clearErrorOnTextChange()
        register_password_confirm_text_input.clearErrorOnTextChange()
    }

    private fun validateInput(email: String, password: String, confirmPassword: String): Boolean {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Timber.e("Invalid email id")
            register_email_text_input.error = getString(R.string.error_invalid_email)
            return false
        }

        if (password.isEmpty()) {
            Timber.e("Invalid password id")
            register_password_text_input.error = getString(R.string.empty_password)
            return false
        }

        if (confirmPassword.isEmpty()) {
            register_password_confirm_text_input.error = getString(R.string.empty_password)
            return false
        }

        if (!password.equals(confirmPassword)) {
            Timber.e("Password doesnt match")
            register_password_confirm_text_input.error = getString(R.string.password_not_match)
            return false
        }

        return true

    }

    private fun hideBottomNavigation() {
        if (activity is MainActivity) {
            Timber.i("Hiding bottom navigation view in register fragment")
            val mainActivity = activity as MainActivity
            mainActivity.hideBottomNavigationView()
        }
    }


}
