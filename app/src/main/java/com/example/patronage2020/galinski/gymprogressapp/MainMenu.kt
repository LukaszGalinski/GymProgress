package com.example.patronage2020.galinski.gymprogressapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainMenu:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenu)

        View.OnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                println(v?.resources?.getIdentifier(v.id.toString(),))
            }
        })

    }
}