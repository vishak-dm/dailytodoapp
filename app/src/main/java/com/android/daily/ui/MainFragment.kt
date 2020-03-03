package com.android.daily.ui


import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.daily.R
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.AuthenticationViewModel
import timber.log.Timber

class MainFragment : androidx.fragment.app.Fragment() {
    private val authenticationViewModelFactory = InjectorUtils.provideAuthenticationViewModelFactory()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val viewmodel = ViewModelProviders.of(this, authenticationViewModelFactory).get(AuthenticationViewModel::class.java)
        viewmodel.getCurrentUserId().observe(viewLifecycleOwner, Observer { userLoggedIn ->
            if (userLoggedIn == null) {
                Timber.i("User has not logged in , moving to authentication screens")
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_authentication_graph)
            } else {
                Timber.i("User has  logged in , moving to today tasks screens")
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_todayTaskFragment)

            }
        })

        return view
    }
}
