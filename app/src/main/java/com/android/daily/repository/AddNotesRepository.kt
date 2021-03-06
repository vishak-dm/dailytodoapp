package com.android.daily.repository

import androidx.lifecycle.MutableLiveData
import com.android.daily.repository.model.NotesData
import com.android.daily.vo.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class AddNotesRepository @Inject constructor(private val firebaseAuth: FirebaseAuth, private val firestoreInstance: FirebaseFirestore) {

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