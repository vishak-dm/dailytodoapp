package com.android.daily.ui.adapters

import android.support.v7.widget.RecyclerView
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.android.daily.R
import com.android.daily.repository.model.GoalsData
import kotlinx.android.synthetic.main.goal_single_layout.view.*
import android.view.LayoutInflater
import com.android.daily.utilities.CommonUtils

typealias GoalClickListener = (GoalsData) -> Unit

class MyGoalsAdapter constructor(private val context: Context, private var goals: List<GoalsData>, private val listenerGoal: GoalClickListener) : RecyclerView.Adapter<MyGoalsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.goal_single_layout, parent, false)
        val viewHolder = MyViewHolder(itemView)
        itemView.setOnClickListener { listenerGoal(goals[viewHolder.adapterPosition]) }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return goals.size
    }

    override fun onBindViewHolder(viewholder: MyViewHolder, position: Int) {
        val goal = goals[position]
        viewholder.goalNameTextView.text = goal.goalName
        viewholder.dueOnTextView.text = CommonUtils.getReadableDaysRemainingString(context,goal.dueDate)
        viewholder.goalNameTagTextView.text = CommonUtils.getIntialsFromString(goal.goalName)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val goalNameTextView = view.goal_name_text_view
        val dueOnTextView = view.due_date_text_view
        val goalNameTagTextView = view.goal_tag_text_view
    }

    //should be called from main thread
    fun setData(data: List<GoalsData>) {
        goals = data
        notifyDataSetChanged()
    }

}