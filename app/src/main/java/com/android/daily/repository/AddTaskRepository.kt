package com.android.daily.repository

import android.arch.lifecycle.MutableLiveData
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddTaskRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()

    companion object {
        @Volatile
        private var instance: AddTaskRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: AddTaskRepository().also { instance = it }
                }
    }


    fun addTaskToGoal(taskName: String, taskDescription: String, selectedDateInMills: Long, goalId: String): MutableLiveData<Resource<Boolean>> {
        val addTaskLiveData = MutableLiveData<Resource<Boolean>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            addTaskLiveData.value = Resource.error("User has not logged in , cannot add tasks", null)
            return addTaskLiveData
        }

        if (goalId.isEmpty()) {
            addTaskLiveData.value = Resource.error("Goals data is not proper , please add goal once again", null)
            return addTaskLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        //create a task model
        val task = TaskData(taskName, taskDescription, selectedDateInMills, "", goalId, false,false)
        val taskDatabaseReference = firestoreInstance.collection(DatabaseReferences.USER_TASK_COLLECTION).document(uid).collection(DatabaseReferences.TASK_SUB_COLLECTION).document()
        //set task id
        task.taskId = taskDatabaseReference.id
        taskDatabaseReference.set(task)
                .addOnSuccessListener {
                    addTaskLiveData.postValue(Resource.success(null))
                }.addOnFailureListener {
                    addTaskLiveData.postValue(Resource.error(it.localizedMessage, null))
                }
        return addTaskLiveData
    }
}