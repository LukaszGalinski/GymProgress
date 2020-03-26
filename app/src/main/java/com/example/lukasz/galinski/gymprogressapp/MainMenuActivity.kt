package com.example.lukasz.galinski.gymprogressapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_menu_layout.*

private const val SQUARES_DURATION = 1000L
private const val SQUARES_ROTATION = 0f
private const val SQUARES_TRANSLATION = 90f
private const val SQUARES_ALPHA = 1f
private const val SQUARES_BASIC_ALPHA = 0.7f
private const val BASIC_ROTATION = 45f
private const val RESET_TRANSITION = 0f
private lateinit var squaresArray: Array<ImageButton>
private lateinit var intense: Intent
class MainMenu:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu_layout)
        squaresArray = arrayOf(Exercises, Records, Statistics, Measures)

        confirm.setOnClickListener {
            startActivity(intense)
        }
    }

    fun categoryPick(v: View) {
        square_name.animate().alpha(SQUARES_ALPHA).duration = SQUARES_DURATION
        confirm.visibility = View.VISIBLE
        confirm.animate().alpha(SQUARES_ALPHA).duration = SQUARES_DURATION
        resetAnimation(squaresArray)
        val resourceName = v.resources.getResourceEntryName(v.id)
        when (v.id) {
            R.id.Exercises -> {
                Exercises.animate().translationY(-SQUARES_TRANSLATION)
                    .rotation(-SQUARES_ROTATION).alpha(SQUARES_ALPHA).duration = SQUARES_DURATION
                intense = Intent(this, WorkoutExercise::class.java)
            }
            R.id.Records -> {
                Records.animate().translationX(SQUARES_TRANSLATION).rotation(-SQUARES_ROTATION)
                    .alpha(SQUARES_ALPHA).duration = SQUARES_DURATION
                intense = Intent(this, DailyActivity::class.java)
            }
            R.id.Statistics -> {
                Statistics.animate().translationY(SQUARES_TRANSLATION).rotation(-SQUARES_ROTATION)
                    .alpha(SQUARES_ALPHA).duration = SQUARES_DURATION
            }
            R.id.Measures -> {
                Measures.animate().translationX(-SQUARES_TRANSLATION)
                    .rotation(-SQUARES_ROTATION).alpha(
                    SQUARES_ALPHA
                ).duration = SQUARES_DURATION
            }
        }
        square_name.text = resourceName
    }

    private fun resetAnimation(squaresArray: Array<ImageButton>) {
        for (i in squaresArray.indices) {
            squaresArray[i].animate().translationY(RESET_TRANSITION).translationX(RESET_TRANSITION).rotation(BASIC_ROTATION).alpha(
                SQUARES_BASIC_ALPHA
            ).duration = SQUARES_DURATION
        }
    }
}