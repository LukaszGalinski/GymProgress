package com.example.lukasz.galinski.gymprogressapp.mainmenu.workout

interface ListViewButtons {
    fun onAddButtonPress(position: Long, weight: String, reps: String)
    fun onRemoveButtonPress(position: Long)
    fun saveNewData(position: Long, weight: String)
}