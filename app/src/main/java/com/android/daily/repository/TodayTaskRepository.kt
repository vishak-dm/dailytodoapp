package com.android.daily.repository

import android.arch.lifecycle.MutableLiveData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TodayTaskRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()

    //Later user dagger  to inject dependencies
    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: TodayTaskRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: TodayTaskRepository().also { instance = it }
                }
    }

    fun getCurrentUserData(): MutableLiveData<Resource<String>> {
        val getCurrentUserDataLiveData = MutableLiveData<Resource<String>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            getCurrentUserDataLiveData.value = Resource.error("User has not logged in , cannot get user details", null)
            return getCurrentUserDataLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        //create a database reference for the user document
        val userDocumentReference = firestoreInstance.collection(DatabaseReferences.USER_COLLECTION).document(uid)
        userDocumentReference.get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.result
                if (document.exists()) {
                    val userData = document.data
                    if (userData != null && userData.contains(DatabaseReferences.USER_NAME)) {
                        //successfully got the user name
                        val userName: String = userData.get(DatabaseReferences.USER_NAME).toString()
                        getCurrentUserDataLiveData.postValue(Resource.success(userName))
                    } else {
                        getCurrentUserDataLiveData.postValue(Resource.error("Could not retrieve user name ", null))
                    }

                } else {
                    getCurrentUserDataLiveData.postValue(Resource.error("Sorry user data doesn't exist for this user id ", null))
                }

            } else {
                //could not retrieve name
                getCurrentUserDataLiveData.postValue(it.exception?.localizedMessage?.let { it1 -> Resource.error(it1, null) })
            }
        }
        return getCurrentUserDataLiveData
    }

}