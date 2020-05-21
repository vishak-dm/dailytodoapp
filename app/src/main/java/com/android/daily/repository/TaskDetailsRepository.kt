package com.android.daily.repository

import androidx.lifecycle.MutableLiveData
import com.android.daily.repository.model.SessionsData
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject


class TaskDetailsRepository @Inject constructor(private val firebaseAuth: FirebaseAuth , private val firestoreInstance : FirebaseFirestore){

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
                .update("c", true)
                .addOnSuccessListener {
                    setTaskCompletedStatusLiveDate.value = Resource.success(true)
                }.addOnFailureListener {
                    setTaskCompletedStatusLiveDate.value = Resource.error(it.localizedMessage, null)
                }
        return setTaskCompletedStatusLiveDate

    }

    fun addTaskSessionTime(taskData: TaskData): MutableLiveData<Resource<Boolean>> {
        val updateSessionTimeLiveData = MutableLiveData<Resource<Boolean>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            updateSessionTimeLiveData.value = Resource.error("User has not logged in , cannot add tasks", null)
            return updateSessionTimeLiveData
        }
        if (taskData.id.isEmpty()) {
            updateSessionTimeLiveData.value = Resource.error("Invalid task data ,please try again later", null)
            return updateSessionTimeLiveData
        }
        val uid = currentUser.uid
        firestoreInstance.collection(DatabaseReferences.USER_TASK_COLLECTION).document(uid)
                .collection(DatabaseReferences.TASK_SUB_COLLECTION).document(taskData.id)
                .set(taskData)
                .addOnSuccessListener {
                    updateSessionTimeLiveData.value = Resource.success(true)
                }.addOnFailureListener {
                    updateSessionTimeLiveData.value = Resource.error(it.localizedMessage, null)
                }
        return updateSessionTimeLiveData

    }
}