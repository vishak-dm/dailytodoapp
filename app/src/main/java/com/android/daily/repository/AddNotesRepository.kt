package com.android.daily.repository

import android.arch.lifecycle.MutableLiveData
import com.android.daily.repository.model.NotesData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddNotesRepository {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firestoreInstance = FirebaseFirestore.getInstance()

    companion object {
        @Volatile
        private var instance: AddNotesRepository? = null

        fun getInstance() =
                instance ?: synchronized(this) {
                    instance ?: AddNotesRepository().also { instance = it }
                }
    }

    fun addNote(note: NotesData): MutableLiveData<Resource<Boolean>> {
        val addNoteLiveData = MutableLiveData<Resource<Boolean>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            addNoteLiveData.value = Resource.error("User has not logged in , cannot add notes", null)
            return addNoteLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        val noteDocumentReference = firestoreInstance.collection(DatabaseReferences.NOTES_COLLECTION).document(uid).collection(DatabaseReferences.NOTES_SUB_COLLECTION).document()
        val noteId = noteDocumentReference.id
        note.id = noteId
        if (note.id.isNullOrEmpty()) {
            addNoteLiveData.value = Resource.error("Invalid note id, not adding note", null)
            return addNoteLiveData
        }
        noteDocumentReference.set(note).addOnSuccessListener {
            addNoteLiveData.postValue(Resource.success(null))
        }.addOnFailureListener {
            addNoteLiveData.postValue(Resource.error(it.localizedMessage, null))
        }
        return addNoteLiveData
    }

    fun updateNote(note: NotesData): MutableLiveData<Resource<Boolean>> {
        val updateNoteLiveData = MutableLiveData<Resource<Boolean>>()
        val currentUser = firebaseAuth.currentUser
        if (currentUser == null) {
            updateNoteLiveData.value = Resource.error("User has not logged in , cannot update notes", null)
            return updateNoteLiveData
        }
        //vl get the uid and store in the firestore
        val uid = currentUser.uid
        firestoreInstance.collection(DatabaseReferences.NOTES_COLLECTION).document(uid)
                .collection(DatabaseReferences.NOTES_SUB_COLLECTION).document(note.id).set(note)
                .addOnSuccessListener {
                    updateNoteLiveData.postValue(Resource.success(null))
                }.addOnFailureListener {
                    updateNoteLiveData.postValue(Resource.error(it.localizedMessage, null))
                }
        return updateNoteLiveData
    }
}