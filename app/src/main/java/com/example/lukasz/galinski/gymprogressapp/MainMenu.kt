package com.example.lukasz.galinski.gymprogressapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mainmenu.*

class MainMenu:AppCompatActivity() {
    private val currentDate = "currentDate"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenu)

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val intent = Intent(applicationContext, WorkoutExercise::class.java)
            intent.putExtra(currentDate,dayOfMonth.toString()+"_"+month.toString()+"_"+year.toString())
            startActivity(intent)
        }
    }
}