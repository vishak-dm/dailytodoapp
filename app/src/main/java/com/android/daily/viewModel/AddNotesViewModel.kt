package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.AddNotesRepository
import com.android.daily.repository.model.NotesData
import com.android.daily.vo.Resource

class AddNotesViewModel internal constructor(private val repository: AddNotesRepository) : ViewModel() {
    fun addNotes(title: String, contents: String, createdTime: Long, labelList: MutableList<String>): MutableLiveData<Resource<Boolean>> {
        val note = NotesData(title, contents)
        note.d = createdTime
        note.nl = labelList
        return repository.addNote(note)
    }

    fun updateNote(title: String, contents: String, createdTime: Long, labelList: MutableList<String>, noteId: String): MutableLiveData<Resource<Boolean>> {
        val note = NotesData(title, contents)
        note.d = createdTime
        note.nl = labelList
        note.id = noteId
        return repository.updateNote(note)
    }
}