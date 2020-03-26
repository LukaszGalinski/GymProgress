package com.example.lukasz.galinski.gymprogressapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.HorizontalCalendarListener
import kotlinx.android.synthetic.main.exercise_row.view.*
import kotlinx.android.synthetic.main.exercises_add_layout.*
import java.util.*
import kotlin.collections.ArrayList


private val groupNames = listOf("Barki","Klatka","Plecy","Triceps","Biceps","Grzbiet","Przedramiona","Brzuch","Pośladki","Uda","Łydki")
private const val FIREBASE_DATABASE_PATH = "workout/username/"
private const val CALENDAR_START_END_DAY = 1
var elementsCounter: Int = 0
class WorkoutExercise:AppCompatActivity() {
    private var isForward = true
    private var datePicked: String? = ""
    private var bodyPartButtons: ArrayList<Button> = arrayListOf()
    val listRecords : MutableList<ExerciseData?> = ArrayList()

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercises_add_layout)
        bodyPartButtons = arrayListOf(Barki, Klatka, Plecy, Triceps, Biceps, Grzbiet, Przedramiona, Brzuch, Pośladki, Uda, Łydki)

        for (i in bodyPartButtons) {
            i.setOnClickListener(openAlertDialogWithPickedPart)
        }
        updateSpinner()
        createHorizontalCalendar()
        loadExercises()

        listview.onItemClickListener = AdapterView.OnItemClickListener { _, view, _, _ ->
            val number = view.series_number.text
            Toast.makeText(this, "you pressed item with id" + number, Toast.LENGTH_SHORT).show()
        }
    }

private fun loadExercises(){
    val ref = firebaseDatabase.reference.child(FIREBASE_DATABASE_PATH)
    ref.addValueEventListener(object: ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            listRecords.clear()
            elementsCounter = p0.childrenCount.toInt()
            for (i in p0.children) {

                val exercise: ExerciseData? = i.getValue(ExerciseData::class.java)
                if (exercise?.musclePartName == spinner.selectedItem.toString()) {
                    listRecords.add(exercise)
                }
            }
            val arrayAdapter = MyAdapter(applicationContext, listRecords)
            listview.adapter = arrayAdapter
        }
        override fun onCancelled(p0: DatabaseError) {
        }
    })
}

    private fun createHorizontalCalendar(){
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH, CALENDAR_START_END_DAY)
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -CALENDAR_START_END_DAY)

        val horizontalCalendar = HorizontalCalendar
            .Builder(this, R.id.calendarView)
            .startDate(startDate.time)
            .endDate(endDate.time)
            .build()

        horizontalCalendar.calendarListener = (object: HorizontalCalendarListener() {
            override fun onDateSelected(date: Date?, position: Int) {
                loadExercises()
            }
        })
    }

    private val openAlertDialogWithPickedPart = View.OnClickListener { v: View? ->
        val databaseReference = firebaseDatabase.getReference(FIREBASE_DATABASE_PATH + datePicked).child(
            elementsCounter.inc().toString())
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.exercise_dialog, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val entryName = v?.resources?.getResourceEntryName(v.id)
        mDialogView.findViewById<TextView>(R.id.partName).text = entryName
        val alert = mBuilder.show()

        mDialogView.findViewById<Button>(R.id.dialogCancel_btn).setOnClickListener {
            alert.dismiss()
        }
        mDialogView.findViewById<Button>(R.id.dialogOk_btn).setOnClickListener {
            val exercise = ExerciseData(
                entryName.toString(),
                mDialogView.findViewById<EditText>(R.id.exercisename_editTxt).text.toString(),
                mDialogView.findViewById<EditText>(R.id.weight_editTxt).text.toString(),
                mDialogView.findViewById<EditText>(R.id.series_editTxt).text.toString(),
                mDialogView.findViewById<EditText>(R.id.reps_editTxt).text.toString(),
                elementsCounter.inc().toLong()
            )
            databaseReference.setValue(exercise)
            alert.dismiss()
        }
        loadExercises()
    }

    private fun changeVisibilityOfElements(tab: ArrayList<Button>) {
        for (i in tab) {
            if (i.isVisible) {
                i.visibility = View.GONE
            } else {
                i.visibility = View.VISIBLE
            }
        }
    }

    fun turnAroundModel(v: View) {
        changeVisibilityOfElements(bodyPartButtons)
        if (isForward) {
            imageView.setImageResource(R.drawable.tyl)
            isForward = false
        } else {
            if (!isForward) {
                imageView.setImageResource(R.drawable.man)
                isForward = true
            }
        }
    }

    private fun editElement(chosenChildExerciseData: ExerciseData){
        val ref = firebaseDatabase.getReference(FIREBASE_DATABASE_PATH + datePicked).child(chosenChildExerciseData.exerciseId.toString())
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.exercise_dialog, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)

        val exerciseName = mDialogView.findViewById<EditText>(R.id.exercisename_editTxt) as TextView
        val exerciseWeight = mDialogView.findViewById<EditText>(R.id.weight_editTxt) as TextView
        val exerciseSeries = mDialogView.findViewById<EditText>(R.id.series_editTxt) as TextView
        val exerciseReps = mDialogView.findViewById<EditText>(R.id.reps_editTxt) as TextView

        exerciseName.text = chosenChildExerciseData.exerciseName
        exerciseWeight.text = chosenChildExerciseData.weight
        exerciseSeries.text = chosenChildExerciseData.seriesNumber
        exerciseReps.text = chosenChildExerciseData.repsNumber

        val alert = mBuilder.show()
        mDialogView.findViewById<Button>(R.id.dialogCancel_btn).setOnClickListener {
            alert.dismiss()
        }
        mDialogView.findViewById<Button>(R.id.dialogOk_btn).setOnClickListener {
            val exercise = ExerciseData(
                chosenChildExerciseData.musclePartName.toString(),
                exerciseName.text.toString(),
                exerciseWeight.text.toString(),
                exerciseSeries.text.toString(),
                exerciseReps.text.toString(),
                chosenChildExerciseData.exerciseId
            )
            ref.setValue(exercise)
            alert.dismiss()
        }
    }
    private fun deleteElement(data: ExerciseData){
        val ref = firebaseDatabase.getReference(FIREBASE_DATABASE_PATH + "$datePicked/${data.exerciseId}")
        ref.removeValue()
    }

    private fun updateSpinner(){
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, groupNames)
        spinner.adapter = arrayAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadExercises()
            }
        }
    }
}
