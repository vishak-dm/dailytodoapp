package com.android.daily.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.daily.R
import com.android.daily.repository.model.NotesData
import kotlinx.android.synthetic.main.single_note_item.view.*
import org.joda.time.DateTime
import org.joda.time.Period

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
        val p = Period(DateTime.now(), DateTime(note.d))
        val days = p.days
        val hrs = Math.abs(p.hours)
        val mins = Math.abs(p.minutes)
        val seconds = Math.abs(p.seconds)
        when {
            days < 0 -> viewholder.noteCreatedTimeTextView.text = String.format(context.getString(R.string.time_ago), Math.abs(days).toString() + "d")
            hrs > 0 -> viewholder.noteCreatedTimeTextView.text = String.format(context.getString(R.string.time_ago), hrs.toString() + "h")
            mins > 0 -> viewholder.noteCreatedTimeTextView.text = String.format(context.getString(R.string.time_ago), mins.toString() + "m")
            else -> viewholder.noteCreatedTimeTextView.text = String.format(context.getString(R.string.time_ago), seconds.toString() + "s")
        }

    }


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteNameTextView: TextView = view.note_name_text_view
        val noteContentsTextView: TextView = view.note_contents_text_view
        val noteCreatedTimeTextView: TextView = view.note_create_time_text_view
    }

    //should be called from main thread
    fun setData(data: List<NotesData>) {
        notes = data
        notifyDataSetChanged()
    }

}