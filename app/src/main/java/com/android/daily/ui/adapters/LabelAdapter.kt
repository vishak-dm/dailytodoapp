package com.android.daily.ui.adapters

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import com.android.daily.R
import kotlinx.android.synthetic.main.single_label_layout.view.*
import java.util.*
import kotlin.collections.ArrayList

class LabelAdapter constructor(private var labels: List<String>) : RecyclerView.Adapter<LabelAdapter.MyViewHolder>() {
    private var choosenLabels = ArrayList<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.single_label_layout, parent, false)
        val viewHolder = MyViewHolder(itemView)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return labels.size
    }

    override fun onBindViewHolder(viewholder: MyViewHolder, position: Int) {
        val label = labels[position]
        viewholder.labelcheckBox.setOnCheckedChangeListener(null)
        viewholder.labelTextView.text = label.capitalize()
        viewholder.labelcheckBox.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked)
                choosenLabels.add(label)
            else
                choosenLabels.remove(label)

        }
    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val labelTextView: TextView = view.label_name_text_view
        val labelcheckBox: CheckBox = view.label_checkbox
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

