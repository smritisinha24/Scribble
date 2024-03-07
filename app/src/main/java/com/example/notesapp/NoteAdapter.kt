package com.example.notesapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class NoteAdapter(
    private var notes: MutableList<Note>,
    private val onEditClickListener: ((Note, position: Int) -> Unit)? = null,
    private val onDeleteClickListener: ((position: Int) -> Unit)? = null
) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var filteredNotes: MutableList<Note> = mutableListOf()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        val editButton: ImageButton = itemView.findViewById(R.id.editButton)
        val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)

        init {
            editButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onEditClickListener?.invoke(notes[position], position)
                }
            }
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener?.invoke(position)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        holder.titleTextView.setTextColor(Color.BLACK)
        holder.contentTextView.setTextColor(Color.BLACK)

        holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, note.colorResId))
    }

    override fun getItemCount(): Int {
        return notes.size
    }
//    fun updateNoteAtPosition(position: Int, editedNote: Note) {
//        notes = newNotes.toMutableList()
//        notifyItemChanged(position)
//    }

    fun updateData(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)

        filteredNotes.clear()
        filteredNotes.addAll(newNotes)

        notifyDataSetChanged()
    }

    fun removeNoteAtPosition(position: Int) {
        if (position in 0 until notes.size) {
            notes.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount)
        }
    }
//    fun updateData(newNotes: List<Note>) {
//        notes.clear()
//        notes.addAll(newNotes)
//        notifyDataSetChanged()
//    }

    private class NoteDiffCallback(
        private val oldList: List<Note>,
        private val newList: List<Note>
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldNote = oldList[oldItemPosition]
            val newNote = newList[newItemPosition]
            return oldNote.title == newNote.title && oldNote.content == newNote.content
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
//
//    fun removeNoteAtPosition(position: Int) {
//        if (position in 0 until notes.size) {
//            notes.removeAt(position)
//            notifyItemRemoved(position)
//        }
//    }
}