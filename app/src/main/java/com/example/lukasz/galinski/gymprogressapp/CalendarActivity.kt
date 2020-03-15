package com.example.lukasz.galinski.gymprogressapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.main_menu_layout.*

private const val CURRENT_DATE = "currentDate"
private const val USERNAME_LABEL = "username"

class CalendarActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu_layout)
        val userName = intent.getStringExtra(USERNAME_LABEL)
        println(userName)
        //welcome_txtView.text = getString(R.string.welcome_user, userName)

      //  calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
    //        val intent = Intent(applicationContext, WorkoutExercise::class.java)
     //       intent.putExtra(CURRENT_DATE,dayOfMonth.toString()+"_"+month+1.toString()+"_"+year.toString())
    //        startActivity(intent)
    //    }


    }

    fun accent(v: View){
        v.setOnClickListener{
            v.animate().translationX(50f).setDuration(1000).interpolator
            confirm.animate().alpha(1f).setDuration(1000)
        }
    }
}