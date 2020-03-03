package com.android.daily.repository

import androidx.lifecycle.MutableLiveData
import com.android.daily.repository.model.GoalsData
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


    fun addTaskToGoal(taskName: String, taskDescription: String, selectedDateInMills: Long, goalId: String?): MutableLiveData<Resource<Boolean>> {
        val addTaskLiveData = MutableLiveData<Resource<Boolean>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            addTaskLiveData.value = Resource.error("User has not logged in , cannot add tasks", null)
            return addTaskLiveData
        }

        if (goalId!!.isEmpty()) {
            addTaskLiveData.value = Resource.error("Goals data is not proper , please add goal once again", null)
            return addTaskLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        //create a task model
        val task = goalId?.let { TaskData(taskName, taskDescription, selectedDateInMills, "", it, false,false) }
        val taskDatabaseReference = firestoreInstance.collection(DatabaseReferences.USER_TASK_COLLECTION).document(uid).collection(DatabaseReferences.TASK_SUB_COLLECTION).document()
        //set task id
        task?.id = taskDatabaseReference.id
        if (task != null) {
            taskDatabaseReference.set(task)
                    .addOnSuccessListener {
                        addTaskLiveData.postValue(Resource.success(null))
                    }.addOnFailureListener {
                        addTaskLiveData.postValue(Resource.error(it.localizedMessage, null))
                    }
        }
        return addTaskLiveData
    }

    fun getAllGoals(): MutableLiveData<Resource<List<GoalsData>>> {
        val getMyGoalsLiveData = MutableLiveData<Resource<List<GoalsData>>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            getMyGoalsLiveData.value = Resource.error("User has not logged in , cannot retrieve your goals", null)
            return getMyGoalsLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        val myGoalList = ArrayList<GoalsData>()
        firestoreInstance.collection(DatabaseReferences.USER_GOALS_COLLECTION).document(uid).collection(DatabaseReferences.GOALS_SUB_COLLECTION).get()
                .addOnSuccessListener {
                    if (it != null && it.documents.isNotEmpty()) {
                        for (document in it.documents) {
                            val goal = document.toObject(GoalsData::class.java)
                            goal?.let { it1 -> myGoalList.add(it1) }
                        }
                    }
                    getMyGoalsLiveData.postValue(Resource.success(myGoalList))
                }.addOnFailureListener {
                    getMyGoalsLiveData.postValue(Resource.error(it.localizedMessage, null))
                }
        return getMyGoalsLiveData
    }
}