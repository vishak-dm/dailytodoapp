package com.android.daily


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.daily.ui.MainActivity
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.AuthenticationViewModel
import timber.log.Timber

class MainFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        val authenticationViewModelFactory = InjectorUtils.provideAuthenticationViewModelFactory()
        val viewmodel = ViewModelProviders.of(this, authenticationViewModelFactory).get(AuthenticationViewModel::class.java)

            viewmodel.getCurrentUserId().observe(viewLifecycleOwner, Observer { userLoggedIn ->
                if (userLoggedIn == null) {
                    Timber.i("User has not logged in , moving to authentication screens")
                    Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_authentication_graph)
                }
            })

        return view
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }

    private fun showBottomNavigationView() {
        if (activity is MainActivity) {
            Timber.i("Showing bottom navigation view in main fragment")
            val mainActivity = activity as MainActivity
            mainActivity.showBottomNavigationView()
        }
    }


}
