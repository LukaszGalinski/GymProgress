package com.example.lukasz.galinski.gymprogressapp

import android.media.Image
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_menu_layout.*

lateinit var squaresArray : Array<ImageButton>

class MainMenu:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu_layout)
        squaresArray = arrayOf(exercise_square, records, charts, square_measure )

    }

    fun categoryPick(v: View){
        square_name.animate().alpha(1f).duration = 1000L
        confirm.animate().alpha(1f).duration = 1000L
        resetAnimation(squaresArray)
        when (v.id){
            R.id.exercise_square -> {
                exercise_square.animate().translationY(-90f).rotation(-90f).alpha(1f).duration = 1000L
            }
            R.id.records -> {
                records.animate().translationX(90f).rotation(-90f).alpha(1f).duration = 1000L
            }
            R.id.charts -> {
                charts.animate().translationY(90f).rotation(-90f).alpha(1f).duration = 1000L
            }
            R.id.square_measure -> {
                square_measure.animate().translationX(-90f).rotation(-90f).alpha(1f).duration = 1000L
            }
        }
    }

    private fun resetAnimation(array: Array<ImageButton>){
        for (i in array.indices){
            array[i].animate().translationY(45f).rotation(45f).alpha(0.8f).duration = 1000L
        }
    }
}