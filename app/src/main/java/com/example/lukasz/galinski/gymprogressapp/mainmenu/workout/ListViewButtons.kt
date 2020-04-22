package com.example.lukasz.galinski.gymprogressapp.mainmenu.workout

interface ListViewButtons {
    fun onAddButtonPress()
    fun onRemoveButtonPress(position: Int)
    fun onEditButtonPress(position: Int)
}