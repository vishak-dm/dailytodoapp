package com.android.daily.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.daily.repository.TaskDetailsRepository
import com.android.daily.repository.model.SessionsData
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource

class TaskTimerViewModel internal constructor(private val repository: TaskDetailsRepository) : ViewModel() {

    fun updateTaskSessionTime(taskData: TaskData): MutableLiveData<Resource<Boolean>> {
        return repository.addTaskSessionTime(taskData)
    }
}