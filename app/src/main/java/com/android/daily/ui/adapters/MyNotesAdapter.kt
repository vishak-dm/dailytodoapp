package com.android.daily.ui.adapters

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.daily.R
import com.android.daily.repository.model.NotesData
import kotlinx.android.synthetic.main.single_note_item.view.*
import org.joda.time.*

typealias NotesClicklistener = (NotesData) -> Unit

class MyNotesAdapter constructor(private val context: Context, private var notes: List<NotesData>, private val noteClickListener: NotesClicklistener) : RecyclerView.Adapter<MyNotesAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.single_note_item, parent, false)
        val viewHolder = MyViewHolder(itemView)
        itemView.setOnClickListener { noteClickListener(notes[viewHolder.adapterPosition]) }
        return viewHolder
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(viewholder: MyViewHolder, position: Int) {
        val note = notes[position]
        viewholder.noteNameTextView.text = note.t.capitalize()
        viewholder.noteContentsTextView.text = note.c.capitalize()
        setNoteCreateDay(viewholder, note)
    }

    private fun setNoteCreateDay(viewholder: MyViewHolder, note: NotesData) {
        val startDate = DateTime.now()
        val endDate = DateTime(note.d)
        val years = Math.abs(Years.yearsBetween(startDate, endDate).years)
        val months = Math.abs(Months.monthsBetween(startDate, endDate).months)
        val days = Math.abs(Days.daysBetween(startDate, endDate).days)
        val hrs = Math.abs(Hours.hoursBetween(startDate, endDate).hours)
        val mins = Math.abs(Minutes.minutesBetween(startDate, endDate).minutes)
        val seconds = Math.abs(Seconds.secondsBetween(startDate, endDate).seconds)
        when {
            years > 0 -> viewholder.noteCreatedTimeTextView.text = String.format(context.getString(R.string.time_ago), years.toString() + " years")
            months > 0 -> viewholder.noteCreatedTimeTextView.text = String.format(context.getString(R.string.time_ago), months.toString() + " months")
            days > 0 -> viewholder.noteCreatedTimeTextView.text = String.format(context.getString(R.string.time_ago), days.toString() + " days")
            hrs > 0 -> viewholder.noteCreatedTimeTextView.text = String.format(context.getString(R.string.time_ago), hrs.toString() + " hours")
            mins > 0 -> viewholder.noteCreatedTimeTextView.text = String.format(context.getString(R.string.time_ago), mins.toString() + " minutes")
            else -> viewholder.noteCreatedTimeTextView.text = String.format(context.getString(R.string.time_ago), seconds.toString() + " seconds")
        }

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteNameTextView: TextView = view.note_name_text_view
        val noteContentsTextView: TextView = view.note_contents_text_view
        val noteCreatedTimeTextView: TextView = view.note_create_time_text_view
        val noteCard: CardView = view.note_card
    }

    //should be called from main thread
    fun setData(data: List<NotesData>) {
        notes = data
        notifyDataSetChanged()
    }

}