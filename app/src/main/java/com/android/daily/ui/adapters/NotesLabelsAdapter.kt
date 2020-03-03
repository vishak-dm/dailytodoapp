package com.android.daily.ui.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.android.daily.R
import kotlinx.android.synthetic.main.single_label_layout.view.*
import kotlinx.android.synthetic.main.single_notes_label_layout.view.*
import kotlin.collections.ArrayList

typealias NoteLabelClickListener = () -> Unit

class NotesLabelsAdapter constructor(private var labels: List<String>, private val listener: NoteLabelClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<NotesLabelsAdapter.MyViewHolder>() {
    private var choosenLabels = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.single_notes_label_layout, parent, false)
        val viewHolder = MyViewHolder(itemView)
        itemView.setOnClickListener {
            listener()
        }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    override fun onBindViewHolder(viewholder: MyViewHolder, position: Int) {
        val label = labels[position]
        viewholder.labelTextView.text = label.capitalize()
    }


    inner class MyViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val labelTextView: TextView = view.label_text_view
    }

    //should be called from main thread
    fun setData(data: List<String>) {
        labels = data
        notifyDataSetChanged()
    }

    fun getChoosenLabels(): List<String> {
        return choosenLabels
    }
}

