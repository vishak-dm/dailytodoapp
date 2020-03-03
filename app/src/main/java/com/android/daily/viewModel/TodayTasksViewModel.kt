package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.TodayTaskRepository
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource

class TodayTasksViewModel internal constructor(private val repository: TodayTaskRepository) : ViewModel() {
    fun getUsername(): MutableLiveData<Resource<String>> {
        return repository.getCurrentUserData()
    }

    fun getTodayTasks(startDate : Long , endDate: Long) : MutableLiveData<Resource<List<TaskData>>>{
        return repository.getTodayTasks(startDate,endDate)
    }

}