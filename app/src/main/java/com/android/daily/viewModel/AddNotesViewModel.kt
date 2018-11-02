package com.android.daily.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.daily.repository.AddNotesRepository
import com.android.daily.repository.model.NotesData
import com.android.daily.vo.Resource

class AddNotesViewModel internal constructor(private val repository: AddNotesRepository) : ViewModel() {
    fun addNotes(title: String, contents: String, createdTime: Long): MutableLiveData<Resource<Boolean>> {
        val note = NotesData(title, contents)
        note.d = createdTime
        return repository.addNote(note)
    }
}