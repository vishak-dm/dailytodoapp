package com.android.daily.ui


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.daily.R
import com.android.daily.utilities.InjectorUtils
import com.android.daily.viewModel.AuthenticationViewModel
import timber.log.Timber


class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_splash, container, false)
        //hide the bottom navigation view
        hideBottomNavigation()
//        val authenticationViewModelFactory = InjectorUtils.provideAuthenticationViewModelFactory()
//        val viewmodel = ViewModelProviders.of(this, authenticationViewModelFactory).get(AuthenticationViewModel::class.java)
//        viewmodel.getCurrentUserId().observe(viewLifecycleOwner, Observer { userLoggedIn ->
//
//            if (userLoggedIn!!) {
//                Timber.i("User has already logged in , moving to main screen")
//                Navigation.findNavController(view).navigate(R.id.action_global_mainFragment)
//            } else {
//                Timber.i("User has not logged in , moving to authentication screens")
//                Navigation.findNavController(view).navigate(R.id.action_splash_to_authentication)
//            }
//        })
        return view
    }

    private fun hideBottomNavigation() {
        if (activity is MainActivity) {
            Timber.i("Hiding bottom navigation view in splash fragment")
            val mainActivity = activity as MainActivity
            mainActivity.hideBottomNavigationView()
        }
    }


}
