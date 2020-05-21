package com.android.daily.ui

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.daily.R
import com.android.daily.repository.model.TaskData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.task_details_bottom_sheet.*


class TaskBottomSheetDialog(private val taskData: TaskData, private val mListener: OnTaskCompleteClicklistener) : BottomSheetDialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.task_details_bottom_sheet, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        task_name_tv_bottom_sheet.text = taskData.n
        task_desc_tv_bottom_sheet.text = taskData.d
        if (taskData.c) complete_task_button_bottom_sheet.visibility = View.GONE else View.VISIBLE
        complete_task_button_bottom_sheet.setOnClickListener {
            mListener.onTaskCompleteClicked(taskData.id)
        }

    }

    interface OnTaskCompleteClicklistener {
        fun onTaskCompleteClicked(taskId: String)
    }


}