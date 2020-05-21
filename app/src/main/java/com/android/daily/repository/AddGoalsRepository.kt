package com.android.daily.repository

import androidx.lifecycle.MutableLiveData
import com.android.daily.repository.model.GoalsData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddGoalsRepository @Inject constructor(private val firebaseAuth: FirebaseAuth, private val firestoreInstance: FirebaseFirestore) {

    fun addGoal(goal: GoalsData): MutableLiveData<Resource<Boolean>> {
        val addGoalLiveData = MutableLiveData<Resource<Boolean>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            addGoalLiveData.value = Resource.error("User has not logged in , cannot add goals", null)
            return addGoalLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        //create a new goal data
        goal.uid = uid
        //now create a  reference for the goal document and store the same in the user collection so that we can get all the goals associated with this user
        val databaseReference = firestoreInstance.collection(DatabaseReferences.USER_GOALS_COLLECTION).document(uid)
                .collection(DatabaseReferences.GOALS_SUB_COLLECTION).document()
        goal.gid = databaseReference.id
        databaseReference.set(goal)
                .addOnSuccessListener {
                    addGoalLiveData.postValue(Resource.success(null))
                }.addOnFailureListener {
                    addGoalLiveData.postValue(Resource.error(it.localizedMessage, null))
                }
        return addGoalLiveData
    }

}