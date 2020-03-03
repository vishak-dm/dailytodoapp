package com.android.daily.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.daily.repository.TaskDetailsRepository
import com.android.daily.vo.Resource

class TaskDetailsViewModel internal constructor(private val repository: TaskDetailsRepository) : ViewModel() {

    fun setTaskCompleteStatus(taskId: String): MutableLiveData<Resource<Boolean>> {
        return repository.setTaskCompleted(taskId)
    }
}