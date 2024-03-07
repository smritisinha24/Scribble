package com.example.notesapp

import java.io.Serializable

data class Note(var title: String, var content: String, val colorResId: Int): Serializable
