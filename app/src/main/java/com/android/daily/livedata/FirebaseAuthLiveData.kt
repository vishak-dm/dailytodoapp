package com.android.daily.livedata

import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth

object FirebaseAuthLiveData : LiveData<Boolean>() {


    private val mFirebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val authStateListener: FirebaseAuth.AuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth: FirebaseAuth ->
        value = firebaseAuth.currentUser != null
    }

    override fun onActive() {
        super.onActive()
        mFirebaseAuth.addAuthStateListener(authStateListener)
    }

    override fun onInactive() {
        super.onInactive()
        mFirebaseAuth.removeAuthStateListener(authStateListener)
    }
}