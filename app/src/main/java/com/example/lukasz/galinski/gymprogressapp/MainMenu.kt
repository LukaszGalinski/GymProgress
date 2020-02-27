package com.example.lukasz.galinski.gymprogressapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.mainmenu.*

private const val CURRENT_DATE = "currentDate"
private const val USERNAME_LABEL = "username"

class MainMenu:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenu)
        val userName = intent.getStringExtra(USERNAME_LABEL)
        println(userName)
        welcome_txtView.text = getString(R.string.welcome_user, userName)

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val intent = Intent(applicationContext, WorkoutExercise::class.java)
            intent.putExtra(CURRENT_DATE,dayOfMonth.toString()+"_"+month+1.toString()+"_"+year.toString())
            startActivity(intent)
        }
    }
}