package com.android.daily.viewModel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.android.daily.repository.TodayTaskRepository
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource

class TodayTasksViewModel internal constructor(private val repository: TodayTaskRepository) : ViewModel() {
    fun getUsename(): MutableLiveData<Resource<String>> {
        return repository.getCurrentUserData()
    }

    fun getTodayTasks(endDate: Long) : MutableLiveData<Resource<List<TaskData>>>{
        return repository.getTodayTasks(endDate)
    }

}