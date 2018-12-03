package com.android.daily.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.daily.R
import kotlinx.android.synthetic.main.mit_single_layout.view.*

class TaskTimeSessionsAdapter constructor(private val context: Context, private var sessions: List<String>) : RecyclerView.Adapter<TaskTimeSessionsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_sessions_single_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    override fun onBindViewHolder(viewholder: MyViewHolder, position: Int) {
        viewholder.taskNameTextView.text = sessions[position]
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val taskNameTextView: TextView = view.mit_task_name_text_view
    }

    //should be called from main thread
    fun setData(data: List<String>) {
        sessions = data
        notifyDataSetChanged()
    }


}