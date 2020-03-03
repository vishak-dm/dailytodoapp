package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.model.TaskData

class SharedViewModel() : ViewModel() {
    private val tasksList = MutableLiveData<List<TaskData>>()
    private val noteLabelList = MutableLiveData<List<String>>()

    fun setTasksList(tasks: List<TaskData>) {
        tasksList.value = tasks
    }

    fun getTasksLiveData(): MutableLiveData<List<TaskData>> {
        return tasksList
    }

    fun setNotesLableList(labels: List<String>) {
        noteLabelList.value = labels
    }

    fun getNoteLabelsLiveData(): MutableLiveData<List<String>> {
        return noteLabelList
    }

}