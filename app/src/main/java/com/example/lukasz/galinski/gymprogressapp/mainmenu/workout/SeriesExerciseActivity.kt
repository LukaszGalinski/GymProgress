package com.example.lukasz.galinski.gymprogressapp.mainmenu.workout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.adapters.SeriesAdapter
import com.example.lukasz.galinski.gymprogressapp.dataclasses.ExerciseData
import com.example.lukasz.galinski.gymprogressapp.dataclasses.SeriesData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.exercises_series_layout.*

private const val FIREBASE_SERIES_REFERENCE = "reference_series"
val listRecords : MutableList<SeriesData?> = ArrayList()
private val emptyRow = SeriesData(0,0,0F)
class SeriesExerciseActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercises_series_layout)
        listRecords.clear()
        val loadedData = intent.getStringArrayListExtra(FIREBASE_SERIES_REFERENCE)
        val seriesReference = loadedData?.get(0)
        val chosenDate = loadedData?.get(1)
        loadSeriesData(seriesReference)
        setTextOnTextViews(chosenDate, seriesReference)
    }

    private fun loadSeriesData(reference: String?){
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseReference = firebaseDatabase.reference.child(reference.toString()).child("series")
        firebaseReference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children) {
                    val exercise: SeriesData? = data.getValue(SeriesData::class.java)
                    listRecords.add(exercise)
                }
                if (listRecords.isEmpty()){
                    listRecords.add(emptyRow)
                }
                val arrayAdapter = SeriesAdapter(applicationContext, listRecords)
                series_listview.adapter = arrayAdapter

                arrayAdapter.setOnItemClickedListener(object: OnItemClicked{
                    override fun onItemClicked() {
                        listRecords.add(emptyRow)
                        arrayAdapter.notifyDataSetChanged()
                    }
                })
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }

    private fun setTextOnTextViews(date: String?, reference: String?){
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseReference = firebaseDatabase.reference.child(reference.toString())
        firebaseReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {
                val data: ExerciseData? = p0.getValue(ExerciseData::class.java)
                exercise_name.text = data?.exerciseName
                partname_label.text = resources.getString(R.string.muscle_part_label, data?.musclePartName)
                date_label.text = resources.getString(R.string.date_label, date)
            }
        })
    }
}
