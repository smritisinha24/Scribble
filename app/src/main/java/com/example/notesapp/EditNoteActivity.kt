package com.example.notesapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText

class EditNoteActivity : AppCompatActivity() {

    private lateinit var editedNote: Note
    private var position: Int = -1

    private lateinit var notes: MutableList<Note>
    private lateinit var noteAdapter: NoteAdapter

    private lateinit var editTextTitle: EditText
    private lateinit var editTextContent: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)

        editTextTitle = findViewById(R.id.editTextTitle)
        editTextContent = findViewById(R.id.editTextContent)

        // Retrieve note and position from the intent
        editedNote = intent.getSerializableExtra("note") as? Note ?: run {
            // Handle the case when "note" extra is not found or cannot be cast to Note
            finish()
            return
        }

        position = intent.getIntExtra("position", -1)

//        val notesArray = intent.getSerializableExtra("notes") as? Array<*>
//        notes = notesArray?.filterIsInstance<Note>()?.toMutableList() ?: mutableListOf()
        notes = (intent.getSerializableExtra("notes") as? Array<Note>)?.toMutableList() ?: mutableListOf()

        noteAdapter = NoteAdapter(notes)

//        notes = (intent.getSerializableExtra("notes") as Array<Note>).toMutableList()
//        noteAdapter = NoteAdapter(notes)

        // Populate the editTexts with existing data
        editTextTitle.setText(editedNote.title)
        editTextContent.setText(editedNote.content)

        val buttonSaveEdit = findViewById<Button>(R.id.buttonSaveEdit)
        buttonSaveEdit.setOnClickListener {
            saveEdit()
        }
    }

    private fun saveEdit() {
        Log.d("EditNote", "Before editing: Title=${editedNote.title}, Content=${editedNote.content}")

        // Save the edited data
        editedNote.title = editTextTitle.text.toString()
        editedNote.content = editTextContent.text.toString()

        Log.d("EditNote", "After editing: Title=${editedNote.title}, Content=${editedNote.content}")

        // Save the edited data
        editedNote.title = editTextTitle.text.toString()
        editedNote.content = editTextContent.text.toString()

        if (editedNote.title.isNotEmpty() && editedNote.content.isNotEmpty()) {
            // Update the note in the list
            val resultIntent = Intent()
            resultIntent.putExtra("editedNote", editedNote)
            resultIntent.putExtra("position", position)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            Log.d("EditNote", "Title or content is empty. Edit cancelled.")
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
    }
}