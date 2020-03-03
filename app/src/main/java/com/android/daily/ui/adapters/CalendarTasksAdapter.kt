package com.android.daily.ui.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.daily.R
import com.android.daily.repository.model.TaskData
import kotlinx.android.synthetic.main.mit_single_layout.view.*

typealias TaskClickListener = (TaskData) -> Unit

class CalendarTasksAdapter  constructor(private val context: Context, private var tasks: List<TaskData>, private val listener: TaskClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<CalendarTasksAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.calendar_task_single_row, parent, false)
        val viewHolder = MyViewHolder(itemView)
        itemView.setOnClickListener { listener(tasks[viewHolder.adapterPosition]) }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return tasks.size
    }

    override fun onBindViewHolder(viewholder: MyViewHolder, position: Int) {
        val task = tasks[position]
        viewholder.taskNameTextView.text = task.n.capitalize()
        viewholder.taskDescription.text = task.d
        if (task.c) {
            viewholder.progressIndicatorImageView.setColorFilter(ContextCompat.getColor(context, R.color.red))
        } else {
            viewholder.progressIndicatorImageView.setColorFilter(ContextCompat.getColor(context, R.color.green))

        }
    }


    inner class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val taskNameTextView: TextView = view.mit_task_name_text_view
        val taskDescription: TextView = view.mit_task_description
        val progressIndicatorImageView: ImageView = view.task_progress_indicator_imageview
    }

    //should be called from main thread
    fun setData(data: List<TaskData>) {
        tasks = data
        notifyDataSetChanged()
    }


}