package com.android.daily.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.daily.R
import com.android.daily.repository.model.TaskData
import com.android.daily.utilities.CommonUtils
import kotlinx.android.synthetic.main.task_single_row_layout.view.*
import org.joda.time.Days
import org.joda.time.LocalDate


typealias TasksClickListener = (TaskData) -> Unit

class TasksListAdapter constructor(private val context: Context, private var tasks: List<TaskData>, private val taskOnCLickListener: TasksClickListener) : RecyclerView.Adapter<TasksListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_single_row_layout, parent, false)
        val viewHolder = MyViewHolder(itemView)
        itemView.setOnClickListener { taskOnCLickListener(tasks[viewHolder.adapterPosition]) }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(viewholder: MyViewHolder, position: Int) {
        val task = tasks[position]
        viewholder.taskNameTextView.text = task.taskName.capitalize()
        viewholder.completeTextView.text = getCompletedStatus(task)
        viewholder.dueOnTextView.text = CommonUtils.getReadableDaysRemainingString(context, task.taskDueDate)
    }

    private fun getCompletedStatus(task: TaskData): String {
        val daysRemaining = Days.daysBetween(LocalDate.now(), LocalDate(task.taskDueDate)).days
        return if (daysRemaining < 0)
            context.getString(R.string.completed)
        else if (task.isCompleted)
            context.getString(R.string.completed)
        else
            context.getString(R.string.in_progress)

    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskNameTextView: TextView = view.task_name_text_view
        val dueOnTextView: TextView = view.task_due_date_text_view
        val completeTextView: TextView = view.completed_text_view
    }

    //should be called from main thread
    fun setData(data: List<TaskData>) {
        tasks = data
        notifyDataSetChanged()
    }

}