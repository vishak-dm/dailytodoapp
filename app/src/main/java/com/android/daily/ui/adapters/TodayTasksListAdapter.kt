package com.android.daily.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.android.daily.R
import com.android.daily.repository.model.TaskData
import kotlinx.android.synthetic.main.select_task_single_view.view.*
import kotlin.collections.ArrayList


class TodayTasksListAdapter constructor(private val context: Context, private var tasks: List<TaskData>) : RecyclerView.Adapter<TodayTasksListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.select_task_single_view, parent, false)
        val viewHolder = MyViewHolder(itemView)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(viewholder: MyViewHolder, position: Int) {
        val task = tasks[position]
        viewholder.taskCheckBox.setOnCheckedChangeListener(null)
        viewholder.taskNameTextView.text = task.taskName.capitalize()
        viewholder.taskDescription.text = task.taskDescription
        viewholder.taskCheckBox.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            task.mit = isChecked
        }
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskNameTextView: TextView = view.today_task_name_text_view
        val taskCheckBox: CheckBox = view.today_task_checkbox
        val taskDescription: TextView = view.today_task_description
    }

    //should be called from main thread
    fun setData(data: List<TaskData>) {
        tasks = data
        notifyDataSetChanged()
    }

    fun getSelectedMits(): List<TaskData> {
        val selectedTasks = ArrayList<TaskData>()
        for (data in tasks) {
            if (data.mit)
                selectedTasks.add(data)
        }

        return selectedTasks
    }

}