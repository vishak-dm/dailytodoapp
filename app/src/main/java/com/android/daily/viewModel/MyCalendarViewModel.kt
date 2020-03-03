package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.MyCalendarRepository
import com.android.daily.repository.model.TaskData
import com.android.daily.vo.Resource

class MyCalendarViewModel internal constructor(private val repository: MyCalendarRepository) : ViewModel() {
    fun getSelectedDateTasks(startDate: Long, endDate: Long): MutableLiveData<Resource<List<TaskData>>> {
        return repository.getSelectedDateTasks(startDate, endDate)
    }
}