package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.AddTaskRepository
import com.android.daily.repository.model.GoalsData
import com.android.daily.vo.Resource

class AddTaskViewModel internal constructor(private val repository : AddTaskRepository) : ViewModel(){
    fun addTask(taskName: String, taskDescription: String, selectedDateInMills: Long, goalId: String?) : MutableLiveData<Resource<Boolean>> {
        return repository.addTaskToGoal(taskName,taskDescription,selectedDateInMills,goalId)
    }

    fun getAllGoalsForUser() : MutableLiveData<Resource<List<GoalsData>>> {
        return repository.getAllGoals()
    }


}