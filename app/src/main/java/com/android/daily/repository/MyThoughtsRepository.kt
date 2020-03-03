package com.android.daily.repository

import androidx.lifecycle.MutableLiveData
import com.android.daily.repository.model.NotesData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MyThoughtsRepository {

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()

    companion object {
        @Volatile
        private var instance: MyThoughtsRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: MyThoughtsRepository().also { instance = it }
                }
    }


    fun getNotes(): MutableLiveData<Resource<List<NotesData>>> {
        val getMyNotesLiveData = MutableLiveData<Resource<List<NotesData>>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            getMyNotesLiveData.value = Resource.error("User has not logged in , cannot retrieve your notes", null)
            return getMyNotesLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        val myNotesList = ArrayList<NotesData>()
        firestoreInstance.collection(DatabaseReferences.NOTES_COLLECTION).document(uid).collection(DatabaseReferences.NOTES_SUB_COLLECTION).get()
                .addOnSuccessListener {
                    if (it != null && it.documents.isNotEmpty()) {
                        for (document in it.documents) {
                            val goal = document.toObject(NotesData::class.java)
                            goal?.let { it1 -> myNotesList.add(it1) }
                        }
                    }
                    getMyNotesLiveData.value = Resource.success(myNotesList)
                }.addOnFailureListener {
                    getMyNotesLiveData.postValue(Resource.error(it.localizedMessage, null))
                }
        return getMyNotesLiveData
    }

}