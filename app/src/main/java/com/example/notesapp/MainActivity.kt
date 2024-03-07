package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class MainActivity : AppCompatActivity() {
//    private val notes = mutableListOf<Note>()
    private var allNotes = mutableListOf<Note>()
    private var filteredNotes = mutableListOf<Note>()
    private lateinit var noteAdapter: NoteAdapter


    private lateinit var recyclerViewNotes: RecyclerView
    private lateinit var buttonAddNote: Button
    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText

    private lateinit var searchView: SearchView

    private val EDIT_NOTE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteAdapter = NoteAdapter(filteredNotes,
            onEditClickListener = { note, position -> showEditDialog(note, position) },
            onDeleteClickListener = { position -> deleteNoteAtPosition(position) }
        )


        recyclerViewNotes = findViewById(R.id.recyclerViewNotes)
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerViewNotes.layoutManager = layoutManager

        recyclerViewNotes.adapter = noteAdapter

        buttonAddNote = findViewById(R.id.buttonAddNote)
        editTextTitle = findViewById(R.id.editTextTitle)
        editTextContent = findViewById(R.id.editTextContent)
        searchView = findViewById(R.id.searchView)

        buttonAddNote.setOnClickListener {
            val title = editTextTitle.text.toString()
            val content = editTextContent.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val colorResId = getRandomColor()
                val note = Note(title, content, colorResId)
                allNotes.add(note)
                applySearchQuery(searchView.query.toString())
                noteAdapter.notifyDataSetChanged()
                clearInputs()
            }

        }
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Handle search query submission if needed
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterNotes(newText)
                return true
            }
        })
    }
    private fun filterNotes(query: String?) {
        val filteredNotes = if (query.isNullOrBlank()) {
            allNotes.toList() // Return a copy of the original list if the query is empty
        } else {
            allNotes.filter { note ->
                note.title.contains(query, ignoreCase = true) || note.content.contains(query, ignoreCase = true)
            }
        }

        noteAdapter.updateData(filteredNotes)
    }


    private fun showEditDialog(note: Note, position: Int) {
        val intent = Intent(this, EditNoteActivity::class.java)
        intent.putExtra("note", note)
        intent.putExtra("position", position)
        startActivityForResult(intent, EDIT_NOTE_REQUEST_CODE)
    }
//    private fun deleteNoteAtPosition(position: Int) {
//        if (position in 0 until notes.size) {
//            notes.removeAt(position)
//            noteAdapter.notifyItemRemoved(position)
//        }
//    }
    private fun deleteNoteAtPosition(position: Int) {
    if (position in 0 until allNotes.size) {
        allNotes.removeAt(position)
        noteAdapter.removeNoteAtPosition(position)
    }
}

    private fun getRandomColor(): Int {
        val colors = arrayOf(
            R.color.noteColor1,
            R.color.noteColor2,
            R.color.noteColor3,
            R.color.noteColor4,
            R.color.noteColor5,
            R.color.noteColor6,
            R.color.noteColor7,
            R.color.noteColor8,
            )

        return colors.random()
    }

    private fun clearInputs() {
        editTextTitle.text.clear()
        editTextContent.text.clear()
    }

    private fun applySearchQuery(query: String) {
        filteredNotes.clear()
        for (note in allNotes) {
            if (note.title.contains(query, true) || note.content.contains(query, true)) {
                filteredNotes.add(note)
            }
        }
//        noteAdapter.updateData(filteredNotes)
        noteAdapter.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_NOTE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val editedNote = data?.getSerializableExtra("editedNote") as? Note
                val position = data?.getIntExtra("position", -1) ?: -1

                if (editedNote != null && position != -1) {
                    allNotes[position] = editedNote
//                    noteAdapter.updateData(allNotes)
                    noteAdapter.notifyItemChanged(position)
                    Log.d("EditNote", "Note edited successfully")
                }
                else {
                    Log.d("EditNote", "Failed to edit note")
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("EditNote", "Edit cancelled")
            }
        }
    }
}