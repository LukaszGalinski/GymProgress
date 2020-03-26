package com.example.lukasz.galinski.gymprogressapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.daily_tasks_layout.*

class DailyActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_tasks_layout)
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            loadData()
        }
    }

    private fun loadData(){
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        val ref = db.reference.child("ja/daily/21")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(data: DataSnapshot) {
                val values: DailyTasksData? = data.getValue(DailyTasksData::class.java)
                textView5.text = values?.water.toString() + "L"
                vegetable_checkbox.isChecked = values?.vegetables!!
                apple_checkbox.isChecked = values.fruit
                practice_checkbox.isChecked = values.gym
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}