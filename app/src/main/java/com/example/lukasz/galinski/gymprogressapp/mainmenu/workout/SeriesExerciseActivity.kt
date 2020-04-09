package com.example.lukasz.galinski.gymprogressapp.mainmenu.workout

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.adapters.SeriesAdapter
import com.example.lukasz.galinski.gymprogressapp.dataclasses.SeriesData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.series_view_layout.*

private const val FIREBASE_SERIES_REFERENCE = "reference_series"
val listRecords : MutableList<SeriesData?> = ArrayList()
class SeriesExerciseActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.series_view_layout)
        val loadedData = intent.getStringArrayListExtra(FIREBASE_SERIES_REFERENCE)
        val seriesReference = loadedData?.get(0)
        val chosenDate = loadedData?.get(1)
        loadSeriesData(chosenDate, seriesReference)
    }

    private fun loadSeriesData(date: String?, reference: String?){
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseReference = firebaseDatabase.reference.child(reference.toString()).child("series")

        firebaseReference.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                listRecords.clear()
                for (data in p0.children) {
                    val exercise: SeriesData? = data.getValue(
                        SeriesData::class.java)
                    listRecords.add(exercise)
                }
                if (listRecords.isEmpty()){
                    listRecords.add(
                        SeriesData(
                            0,
                            0,
                            0F
                        )
                    )
                }
                val arrayAdapter =
                    SeriesAdapter(
                        applicationContext,
                        listRecords
                    )
                series_listview.adapter = arrayAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        date_label.text = date
    }
}