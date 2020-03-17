package com.example.lukasz.galinski.gymprogressapp

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_menu_layout.*

private const val SQUARES_DURATION = 1000L
private const val SQUARES_ROTATION = 135f
private const val SQUARES_TRANSLATION = 90f
private const val SQUARES_ALPHA = 1f
private const val SQUARES_BASIC_ALPHA = 0.7f
private const val BASIC_ROTATION = 45f
private const val RESET_TRANSITION = 0f
private lateinit var squaresArray: Array<ImageButton>
class MainMenu:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu_layout)
        squaresArray = arrayOf(exercise_square, records, charts, square_measure)
    }

    fun categoryPick(v: View) {
        square_name.animate().alpha(SQUARES_ALPHA).duration = SQUARES_DURATION
        confirm.animate().alpha(SQUARES_ALPHA).duration = SQUARES_DURATION
        resetAnimation(squaresArray)
        val resourceName = v.resources.getResourceEntryName(v.id)
        when (v.id) {
            R.id.exercise_square -> {
                exercise_square.animate().translationY(-SQUARES_TRANSLATION)
                    .rotation(-SQUARES_ROTATION).alpha(SQUARES_ALPHA).duration = SQUARES_DURATION
            }
            R.id.records -> {
                records.animate().translationX(SQUARES_TRANSLATION).rotation(-SQUARES_ROTATION)
                    .alpha(SQUARES_ALPHA).duration = SQUARES_DURATION
            }
            R.id.charts -> {
                charts.animate().translationY(SQUARES_TRANSLATION).rotation(-SQUARES_ROTATION)
                    .alpha(SQUARES_ALPHA).duration = SQUARES_DURATION
            }
            R.id.square_measure -> {
                square_measure.animate().translationX(-SQUARES_TRANSLATION)
                    .rotation(-SQUARES_ROTATION).alpha(
                    SQUARES_ALPHA
                ).duration = SQUARES_DURATION
            }
        }
        square_name.text = resourceName
    }

    private fun resetAnimation(squaresArray: Array<ImageButton>) {
        for (i in squaresArray.indices) {
            squaresArray[i].animate().translationY(RESET_TRANSITION).rotation(BASIC_ROTATION).alpha(
                SQUARES_BASIC_ALPHA
            ).duration = SQUARES_DURATION
        }
    }
}