package com.android.daily.viewModel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.daily.repository.UserRepository
import com.android.daily.vo.Resource

/**
 * This class handles all the authentication associated with main activity
 * It tells whether user is signed in , logsin user , creates user
 */

class AuthenticationViewModel internal constructor(private val userRepository: UserRepository) : ViewModel() {


    fun getCurrentUserId(): LiveData<String> {
        return userRepository.isUserLoggedIn()
    }

    fun createUser(email: String, password: String): MutableLiveData<Resource<String>> {
        //this should talk to repository
        return userRepository.createUserWithEmailAndPassword(email, password)
    }

    fun loginUserWithEmailAndPassword(email: String, password: String): MutableLiveData<Resource<String>> {
        return userRepository.loginUserWithEmailAndPassword(email, password)
    }

    fun saveUserDetails(name: String): MutableLiveData<Resource<Boolean>> {
        return userRepository.saveUserDetails(name)
    }

}