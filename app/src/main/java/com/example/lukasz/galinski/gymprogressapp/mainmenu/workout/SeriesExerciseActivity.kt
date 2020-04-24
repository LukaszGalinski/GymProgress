package com.example.lukasz.galinski.gymprogressapp.mainmenu.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.adapters.SeriesAdapter
import com.example.lukasz.galinski.gymprogressapp.dataclasses.ExerciseData
import com.example.lukasz.galinski.gymprogressapp.dataclasses.SeriesData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.exercises_series_edit_dialog.*
import kotlinx.android.synthetic.main.exercises_series_layout.*

private const val INTENT_REFERENCE = "reference_series"
private const val FIREBASE_SERIES_REFERENCE = "series/"
var listRecords : MutableList<SeriesData?> = ArrayList()
private val emptyRow = SeriesData(0,0,0F)
var ref: String? = ""
class SeriesExerciseActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercises_series_layout)
        val loadedData = intent.getStringArrayListExtra(INTENT_REFERENCE)
        val seriesReference = loadedData?.get(0)
        ref = seriesReference
        val chosenDate = loadedData?.get(1)
        loadSeriesData(seriesReference)
        setTextOnTextViews(chosenDate, seriesReference)

        daily_save.setOnClickListener {
            saveDataIntoFirebase(seriesReference)
        }
    }

    private fun loadSeriesData(reference: String?) {
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        val firebaseReference = firebaseDatabase.reference.child(reference.toString()).child(FIREBASE_SERIES_REFERENCE)
        firebaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                listRecords.clear()
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
                    Toast.makeText(this@SeriesExerciseActivity, resources.getText(R.string.last_element_remove_errror), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onEditButtonPress(position: Int) {
                val mDialogView = LayoutInflater.from(this@SeriesExerciseActivity).inflate(R.layout.exercises_series_edit_dialog, null)
                val mBuilder = AlertDialog.Builder(this@SeriesExerciseActivity).setView(mDialogView)
                val currentData: SeriesData? = listRecords[position]

                val exerciseWeight = mDialogView.findViewById<EditText>(R.id.weight_alert)
                val exerciseReps = mDialogView.findViewById<EditText>(R.id.reps_alert)
                val positiveButton = mDialogView.findViewById<Button>(R.id.sendBtn)
                val cancelButton = mDialogView.findViewById<Button>(R.id.cancelBtn)
                clearOnEditTextPress(arrayOf(exerciseReps, exerciseWeight))

                exerciseWeight.setText(currentData?.weight.toString())
                exerciseReps.setText(currentData?.repsNumber.toString())
                val alert = mBuilder.show()

                positiveButton.setOnClickListener {
                    val newData = SeriesData(position, exerciseReps.text.toString().toInt(), exerciseWeight.text.toString().toFloat())
                    listRecords[position] = newData
                    adapter.notifyDataSetChanged()
                    alert.dismiss()
                }
                cancelButton.setOnClickListener {
                    alert.dismiss()
                }
            }
        })
    }

    private fun saveDataIntoFirebase(reference: String?){
        val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
        for (i in listRecords.indices){
            val seriesId = listRecords[i]?.seriesId
            val addDataReference = firebaseDatabase.reference.child("$reference/$FIREBASE_SERIES_REFERENCE/$seriesId")
            addDataReference.setValue(listRecords[i])
        }
    }

    private fun clearOnEditTextPress(editTexts: Array<EditText>){
        for (i in editTexts.indices){
            editTexts[i].setOnClickListener{
                editTexts[i].text.clear()
            }
        }
    }
}


