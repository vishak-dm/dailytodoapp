package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.TaskDetailsRepository
import com.android.daily.repository.model.SessionsData
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource
import javax.inject.Inject

class TaskTimerViewModel @Inject constructor(private val repository: TaskDetailsRepository) : ViewModel() {

    fun updateTaskSessionTime(taskData: TaskData): MutableLiveData<Resource<Boolean>> {
        return repository.addTaskSessionTime(taskData)
    }
}