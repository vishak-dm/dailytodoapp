package com.android.daily.ui.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.daily.R
import com.android.daily.repository.model.SessionsData
import kotlinx.android.synthetic.main.task_sessions_single_row.view.*
import org.joda.time.LocalDate

class TaskTimeSessionsAdapter constructor(private var sessions: List<SessionsData>) : androidx.recyclerview.widget.RecyclerView.Adapter<TaskTimeSessionsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.task_sessions_single_row, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return sessions.size
    }

    override fun onBindViewHolder(viewholder: MyViewHolder, position: Int) {
        val sessionsData = sessions[position]
        viewholder.sessionNameTextView.text = sessionsData.n.capitalize()
        viewholder.sessionDescriptionTextView.text = sessionsData.d.capitalize()
        viewholder.sessionStartDateTextView.text = LocalDate(sessionsData.t).toString("MMM dd, yyyy")
        setDuration(viewholder, sessionsData.l)
    }

    private fun setDuration(viewholder: MyViewHolder, duration: Long) {
        val minutesUntilFinished = duration / 60
        val secondsInMinuteUntilFinished = duration - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        viewholder.sessionDurationTextView.text = minutesUntilFinished.toString() + "m " + secondsStr + "s"
    }


    inner class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val sessionNameTextView: TextView = view.session_name_text_view
        val sessionDescriptionTextView: TextView = view.session_description_text_view
        val sessionStartDateTextView: TextView = view.session_start_date_text_view
        val sessionDurationTextView: TextView = view.session_duration_text_view

    }

    //should be called from main thread
    fun setData(data: List<SessionsData>) {
        sessions = data
        notifyDataSetChanged()
    }


}