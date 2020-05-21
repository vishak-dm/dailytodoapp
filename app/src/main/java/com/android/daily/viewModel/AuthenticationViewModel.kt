package com.android.daily.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.UserRepository
import com.android.daily.vo.Resource
import javax.inject.Inject

/**
 * This class handles all the authentication associated with main activity
 * It tells whether user is signed in , logsin user , creates user
 */

class AuthenticationViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {


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