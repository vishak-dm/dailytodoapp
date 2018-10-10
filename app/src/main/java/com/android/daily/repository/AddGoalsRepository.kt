package com.android.daily.repository

import android.arch.lifecycle.MutableLiveData
import com.android.daily.repository.model.GoalsData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddGoalsRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()

    //Later user dagger  to inject dependencies
    companion object {

        // For Singleton instantiation
        @Volatile
        private var instance: AddGoalsRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: AddGoalsRepository().also { instance = it }
                }
    }

    fun addGoal(goalName: String, goalDescription: String, selectedDateInMills: Long): MutableLiveData<Resource<Boolean>> {
        val addGoalLiveData = MutableLiveData<Resource<Boolean>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            addGoalLiveData.value = Resource.error("User has not logged in , cannot add goals", null)
            return addGoalLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        //create a new goal data
        val goal = GoalsData(goalName, goalDescription, selectedDateInMills, uid)
        //now create a  reference for the goal document and store the same in the user collection so that we can get all the goals associated with this user
        val databaseReference = firestoreInstance.collection(DatabaseReferences.USER_GOALS_COLLECTION).document(uid)
                .collection(DatabaseReferences.GOALS_SUB_COLLECTION).document()
        goal.goalId = databaseReference.id
        databaseReference.set(goal)
                .addOnSuccessListener {
                    addGoalLiveData.postValue(Resource.success(null))
                }.addOnFailureListener {
                    addGoalLiveData.postValue(Resource.error(it.localizedMessage, null))
                }
        return addGoalLiveData
    }

}