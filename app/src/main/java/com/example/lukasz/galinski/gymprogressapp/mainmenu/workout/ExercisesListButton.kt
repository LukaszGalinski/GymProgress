package com.example.lukasz.galinski.gymprogressapp.mainmenu.workout

interface ExercisesListButton {
    fun onAddButtonPress()
    fun onRemoveButtonPress(position: Int)
    fun onEditButtonPress(position: Int)
}