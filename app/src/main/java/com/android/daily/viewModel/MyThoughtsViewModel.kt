package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.MyThoughtsRepository
import com.android.daily.repository.model.NotesData
import com.android.daily.vo.Resource
import javax.inject.Inject

class MyThoughtsViewModel @Inject constructor(private val repository: MyThoughtsRepository) : ViewModel() {
    fun getMyNotes():MutableLiveData<Resource<List<NotesData>>>{
        return repository.getNotes()
    }
}