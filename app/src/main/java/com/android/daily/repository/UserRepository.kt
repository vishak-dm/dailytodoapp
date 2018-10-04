package com.android.daily.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class UserRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()

    //Later user dagger  to inject dependencies
    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: UserRepository().also { instance = it }
                }
    }


    fun isUserLoggedIn(): LiveData<String> {
        val isUserSignedInLiveData = MutableLiveData<String>()
        isUserSignedInLiveData.postValue(firebaseAuth.currentUser?.uid)
        return isUserSignedInLiveData
    }

    fun createUserWithEmailAndPassword(email: String, password: String): MutableLiveData<Resource<String>> {
        val createUserLiveData = MutableLiveData<Resource<String>>()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {

            if (!it.isSuccessful)
                createUserLiveData.postValue(Resource.error(it.exception?.localizedMessage!!, null))
            else
                createUserLiveData.postValue(Resource.success(it.result.user.uid))
        }
        return createUserLiveData
    }

    fun loginUserWithEmailAndPassword(email: String, password: String): MutableLiveData<Resource<String>> {
        val loginUserLiveData = MutableLiveData<Resource<String>>()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (!it.isSuccessful)
                loginUserLiveData.postValue(Resource.error(it.exception?.localizedMessage!!, null))
            else
                loginUserLiveData.postValue(Resource.success(it.result.user.uid))
        }
        return loginUserLiveData
    }

    fun saveUserDetails(name: String): MutableLiveData<Resource<Boolean>> {
        val saveUserDetailsLiveData = MutableLiveData<Resource<Boolean>>()
        //first check if user is logged in .. or else we need to send an error
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            saveUserDetailsLiveData.value = Resource.error("User has not logged in , cannot save user details", null)
            return saveUserDetailsLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        val data = HashMap<String, String>()
        data.put(DatabaseReferences.USER_NAME, name)
        val userDocumentReference = firestoreInstance.collection(DatabaseReferences.USER_COLLECTION).document(uid)
        userDocumentReference.set(data as Map<String, String>, SetOptions.merge()).addOnSuccessListener {
            saveUserDetailsLiveData.postValue(Resource.success(null))
        }.addOnFailureListener {
            saveUserDetailsLiveData.postValue(Resource.error(it.localizedMessage, null))
        }

        return saveUserDetailsLiveData

    }


}