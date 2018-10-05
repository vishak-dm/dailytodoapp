package com.android.daily.repository

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.android.daily.repository.model.GoalsData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyGoalsRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()

    companion object {
        @Volatile
        private var instance: MyGoalsRepository? = null

        fun getInstance() =
                MyGoalsRepository.instance ?: synchronized(this) {
                    MyGoalsRepository.instance
                            ?: MyGoalsRepository().also { MyGoalsRepository.instance = it }
                }
    }


    fun getMyGoals(): MutableLiveData<Resource<List<GoalsData>>> {
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
                        getMyGoalsLiveData.postValue(Resource.success(myGoalList))
                    }
                }.addOnFailureListener {
                    getMyGoalsLiveData.postValue(Resource.error(it.localizedMessage, null))
                }
        return getMyGoalsLiveData

    }

}