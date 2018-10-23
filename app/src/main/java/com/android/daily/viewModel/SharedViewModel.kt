package com.android.daily.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.daily.repository.model.TaskData

class SharedViewModel() : ViewModel() {
    private val tasksList = MutableLiveData<List<TaskData>>()

    fun setTasksList(tasks: List<TaskData>) {
        tasksList.value = tasks
    }

    fun getTasksLiveData(): MutableLiveData<List<TaskData>> {
        return tasksList
    }

}