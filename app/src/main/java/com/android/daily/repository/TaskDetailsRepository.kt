package com.android.daily.repository

import android.arch.lifecycle.MutableLiveData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TaskDetailsRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()

    companion object {
        @Volatile
        private var instance: TaskDetailsRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: TaskDetailsRepository().also { instance = it }
                }
    }

    fun setTaskCompleted(taskId: String): MutableLiveData<Resource<Boolean>> {
        val setTaskCompletedStatusLiveDate = MutableLiveData<Resource<Boolean>>()

        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            setTaskCompletedStatusLiveDate.value = Resource.error("User has not logged in , cannot add tasks", null)
            return setTaskCompletedStatusLiveDate
        }
        if (taskId.isEmpty()) {
            setTaskCompletedStatusLiveDate.value = Resource.error("Invalid task data ,please try again later", null)
            return setTaskCompletedStatusLiveDate
        }
        val uid = currentUser.uid
        firestoreInstance.collection(DatabaseReferences.USER_TASK_COLLECTION).document(uid)
                .collection(DatabaseReferences.TASK_SUB_COLLECTION).document(taskId)
                .update("completed", true)
                .addOnSuccessListener {
                    setTaskCompletedStatusLiveDate.value = Resource.success(true)
                }.addOnFailureListener {
                    setTaskCompletedStatusLiveDate.value = Resource.error(it.localizedMessage, null)
                }
        return setTaskCompletedStatusLiveDate

    }
}