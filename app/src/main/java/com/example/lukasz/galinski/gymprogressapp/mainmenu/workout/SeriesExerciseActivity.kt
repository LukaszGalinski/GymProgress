package com.example.lukasz.galinski.gymprogressapp.mainmenu.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.adapters.SeriesAdapter
import com.example.lukasz.galinski.gymprogressapp.dataclasses.ExerciseData
import com.example.lukasz.galinski.gymprogressapp.dataclasses.SeriesData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.exercises_series_layout.*
import org.w3c.dom.Text


private const val FIREBASE_SERIES_REFERENCE = "reference_series"
var listRecords : MutableList<SeriesData?> = ArrayList()
private val emptyRow = SeriesData(0,0,0F)
var ref: String? = ""
class SeriesExerciseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercises_series_layout)
        //listRecords.clear()
        val loadedData = intent.getStringArrayListExtra(FIREBASE_SERIES_REFERENCE)
        val seriesReference = loadedData?.get(0)
        ref = seriesReference
        val chosenDate = loadedData?.get(1)
        loadSeriesData(seriesReference)
        setTextOnTextViews(chosenDate, seriesReference)
    }

    private fun loadSeriesData(reference: String?) {
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseReference =
            firebaseDatabase.reference.child(reference.toString()).child("series")
        firebaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val exercise: SeriesData? = data.getValue(SeriesData::class.java)
                    listRecords.add(exercise)
                }
                if (listRecords.isEmpty()) {
                    listRecords.add(emptyRow)
                }
                series_listview.layoutManager = LinearLayoutManager(this@SeriesExerciseActivity)
                val arrayAdapter = SeriesAdapter(listRecords)
                series_listview.adapter = arrayAdapter
                customAdapterButtonsSet(arrayAdapter)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun setTextOnTextViews(date: String?, reference: String?) {
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseReference = firebaseDatabase.reference.child(reference.toString())
        firebaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val data: ExerciseData? = p0.getValue(ExerciseData::class.java)
                exercise_name.text = data?.exerciseName
                partname_label.text =
                    resources.getString(R.string.muscle_part_label, data?.musclePartName)
                date_label.text = resources.getString(R.string.date_label, date)
            }
        })
    }

    private fun customAdapterButtonsSet(adapter: SeriesAdapter) {
        adapter.setOnItemClickedListener(object : ListViewButtons {
            override fun onAddButtonPress() {
                listRecords.add(emptyRow)
                adapter.notifyDataSetChanged()
            }

            override fun onRemoveButtonPress(position: Int) {
                if (listRecords.size > 1) {
                    listRecords.removeAt(position)
                    adapter.notifyDataSetChanged()
                }else{
                    Toast.makeText(this@SeriesExerciseActivity, "You cannot remove the the last element...", Toast.LENGTH_SHORT).show()
                }

            }

            override fun onEditButtonPress(position: Int) {
                val mDialogView = LayoutInflater.from(this@SeriesExerciseActivity).inflate(R.layout.exercises_series_edit_dialog, null)
                val mBuilder = AlertDialog.Builder(this@SeriesExerciseActivity).setView(mDialogView)
                val currentData: SeriesData? = listRecords[position]

                val exerciseWeight = mDialogView.findViewById<TextView>(R.id.weight_alert)
                val exerciseReps = mDialogView.findViewById<TextView>(R.id.reps_alert)

                val positiveButton = mDialogView.findViewById<Button>(R.id.sendBtn)
                exerciseWeight.text = currentData?.weight.toString()
                exerciseReps.text = currentData?.repsNumber.toString()
                val alert = mBuilder.show()

                positiveButton.setOnClickListener {
                    val newData = SeriesData(position, exerciseReps.text.toString().toInt(), exerciseWeight.text.toString().toFloat())
                    listRecords[position] = newData
                    adapter.notifyDataSetChanged()
                    alert.dismiss()
                }
            }
        })
    }
}


