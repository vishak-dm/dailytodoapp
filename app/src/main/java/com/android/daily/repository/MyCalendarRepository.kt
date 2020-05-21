package com.android.daily.repository

import androidx.lifecycle.MutableLiveData
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class MyCalendarRepository @Inject constructor(private val firebaseAuth: FirebaseAuth , private val firestoreInstance: FirebaseFirestore) {

    fun getSelectedDateTasks(startDate: Long, endDate: Long): MutableLiveData<Resource<List<TaskData>>> {
        val getSelectedDateTaskLiveData = MutableLiveData<Resource<List<TaskData>>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            getSelectedDateTaskLiveData.value = Resource.error("User has not logged in , cannot get user details", null)
            return getSelectedDateTaskLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        val tasksList = ArrayList<TaskData>()
        firestoreInstance.collection(DatabaseReferences.USER_TASK_COLLECTION).document(uid).collection(DatabaseReferences.TASK_SUB_COLLECTION).whereGreaterThan("dd", startDate).whereLessThan("dd", endDate).get().addOnSuccessListener {
            if (it != null && it.documents.isNotEmpty()) {
                for (document in it.documents) {
                    val task = document.toObject(TaskData::class.java)
                    task?.let { it1 -> tasksList.add(it1) }
                }
                getSelectedDateTaskLiveData.postValue(Resource.success(tasksList))
            } else {
                getSelectedDateTaskLiveData.postValue(Resource.error("No tasks found for the selected Date", null))
            }
        }
        return getSelectedDateTaskLiveData

    }
}