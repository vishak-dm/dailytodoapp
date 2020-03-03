package com.android.daily.repository

import androidx.lifecycle.MutableLiveData
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GoalDetailsRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()

    //Later user dagger  to inject dependencies
    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: GoalDetailsRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: GoalDetailsRepository().also { instance = it }
                }
    }

    fun getTasksForGoal(goalId: String): MutableLiveData<Resource<List<TaskData>>> {
        val getTasksLiveData = MutableLiveData<Resource<List<TaskData>>>()

        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            getTasksLiveData.value = Resource.error("User has not logged in , cannot add tasks", null)
            return getTasksLiveData
        }

        if (goalId.isEmpty()) {
            getTasksLiveData.value = Resource.error("Sorry couldnot find tasks for this particular goal", null)
            return getTasksLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid

        val tasksList = ArrayList<TaskData>()

        firestoreInstance.collection(DatabaseReferences.USER_TASK_COLLECTION).document(uid).collection(DatabaseReferences.TASK_SUB_COLLECTION).whereEqualTo("gid" ,goalId).get()
                .addOnSuccessListener {
                    if (it != null && it.documents.isNotEmpty()) {
                        for (document in it.documents) {
                            val goal = document.toObject(TaskData::class.java)
                            goal?.let { it1 -> tasksList.add(it1) }
                        }
                        getTasksLiveData.postValue(Resource.success(tasksList))
                    }else{
                        getTasksLiveData.postValue(Resource.error("No tasks added",null))
                    }
                }.addOnFailureListener {
                    getTasksLiveData.postValue(Resource.error(it.localizedMessage, null))
                }

        return getTasksLiveData

    }
}