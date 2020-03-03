package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.TodayTaskRepository
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource

class AddMitViewModel internal constructor(private val repository:TodayTaskRepository) : ViewModel(){
    fun addMitTasks(todayTasks: List<TaskData>) : MutableLiveData<Resource<Boolean>> {
        return repository.addMitTasks(todayTasks)
    }

}