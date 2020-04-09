package com.example.lukasz.galinski.gymprogressapp.mainmenu.daily

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.dataclasses.DailyTasksData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.daily_tasks_layout.*
import java.text.SimpleDateFormat
import java.util.*

private const val REFERENCE_PATCH_NAME = "daily/"
private const val WATER_VALUE_REFERENCE = "water"
private const val DATE_FORMAT = "dd-MM-yyyy"
var water: Float = 0f
var date: String = ""
var user : String = ""
lateinit var ref: DatabaseReference
class DailyActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.daily_tasks_layout)
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        user = getUserEmail()
        date = SimpleDateFormat(
            DATE_FORMAT, Locale.getDefault()).format(Date())
        ref = db.reference.child(
            REFERENCE_PATCH_NAME
        ).child("$date/$user")
        loadData(ref)

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            date = "$dayOfMonth-$month-$year"
            ref = db.reference.child(
                REFERENCE_PATCH_NAME
            ).child("$date/$user")
            loadData(ref)
        }

        daily_save.setOnClickListener {
            saveData(ref)
        }
    }

    private fun loadData(ref: DatabaseReference){
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(data: DataSnapshot) {
                val values: DailyTasksData? = data.getValue(
                    DailyTasksData::class.java)
                water = values?.water ?: 0f
                textView5.text = resources.getString(
                    R.string.water_zero_information,
                    water
                )
                vegetable_checkbox.isChecked = values?.vegetables ?: false
                apple_checkbox.isChecked = values?.fruit ?: false
                practice_checkbox.isChecked = values?.gym ?: false
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun getUserEmail(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        return user.toString()
    }

    fun waterChange(v: View) {
        val waterEditText = findViewById<EditText>(R.id.water_operation_value)
        if (waterEditText.text.isEmpty()) {
            Toast.makeText(this, resources.getString(R.string.water_zero_information), Toast.LENGTH_SHORT).show()
        } else {
            val operationValue: Float = water_operation_value.text.toString().toFloat()
            val firebaseDatabase = FirebaseDatabase.getInstance()
            val ref = firebaseDatabase.reference.child(REFERENCE_PATCH_NAME).child("$date/$user/$WATER_VALUE_REFERENCE")

            when (v.id) {
                R.id.plus_button -> water += operationValue
                R.id.minus_button -> {
                    water -= operationValue
                    if (water <=0) water = 0f
                }
            }
            ref.setValue(water)
            waterEditText.text.clear()
        }
    }

    private fun saveData(ref: DatabaseReference){
        val vegetableStatus = vegetable_checkbox.isChecked
        val fruitsStatus = apple_checkbox.isChecked
        val gymStatus = practice_checkbox.isChecked
        val values: DailyTasksData? =
            DailyTasksData(
                water,
                vegetableStatus,
                fruitsStatus,
                gymStatus
            )
        ref.setValue(values)
    }
}