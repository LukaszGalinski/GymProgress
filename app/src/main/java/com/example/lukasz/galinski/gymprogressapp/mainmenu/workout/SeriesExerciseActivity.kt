package com.example.lukasz.galinski.gymprogressapp.mainmenu.workout

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.adapters.SeriesAdapter
import com.example.lukasz.galinski.gymprogressapp.dataclasses.ExerciseData
import com.example.lukasz.galinski.gymprogressapp.dataclasses.SeriesData
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.exercises_series_layout.*
import kotlinx.android.synthetic.main.exercises_series_list_row.*

private const val FIREBASE_SERIES_REFERENCE = "reference_series"
var listRecords : MutableList<SeriesData?> = ArrayList()
private val emptyRow = SeriesData(0,0,0F)
var ref: String? = ""
class SeriesExerciseActivity: AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercises_series_layout)
        //listRecords.clear()
        val loadedData = intent.getStringArrayListExtra(FIREBASE_SERIES_REFERENCE)
        val seriesReference = loadedData?.get(0)
        ref  = seriesReference
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
                    listRecords.
                }
                if (listRecords.isEmpty()){
                    listRecords.add(emptyRow)
                }
                val arrayAdapter = SeriesAdapter(applicationContext, listRecords)
                series_listview.adapter = arrayAdapter
                customAdapterButtonsSet(arrayAdapter)
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

    private fun customAdapterButtonsSet(adapter: SeriesAdapter){
        adapter.setOnItemClickedListener(object: ListViewButtons{

            override fun onAddButtonPress(position: Long, weight: String, reps: String) {
                val modiefiedRecord = SeriesData(position.toInt(), reps.toInt(), weight.toFloat() )
                listRecords[position.toInt()-1] = modiefiedRecord
                listRecords.add(emptyRow)
                adapter.notifyDataSetChanged()
            }
             //   val view: View = series_listview.getChildAt(position.toInt())
           //     val editText: EditText = view.findViewById(R.id.weight)
            //    val string = editText.text.toString()
          //      listRecords.set(position.toInt(), SeriesData(position.toInt(),1,string.toFloat()))
        //        println("pozycja: + " +listRecords.elementAt(position.toInt())?.weight)

        //        println("listsize: " + position)
        //        println("list: " + listRecords)

            override fun onRemoveButtonPress(position: Long) {
           //     listRecords.removeAt(position.toInt())
         //       adapter.notifyDataSetChanged()
          //      println("removed: " + listRecords)
            }

            override fun saveNewData(position: Long, weight: String) {
                println("przed: " + listRecords)
                    listRecords[position.toInt()]?.weight = weight.toFloat()
                adapter.notifyDataSetChanged()
                println("po: " + listRecords)
                }

             //   listRecords.clear()
          //      listRecords.addAll(arrayList)
            //    println("lista: " + listRecords)
        })
    }
}
