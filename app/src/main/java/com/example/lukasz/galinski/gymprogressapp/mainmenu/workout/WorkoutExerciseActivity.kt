package com.example.lukasz.galinski.gymprogressapp.mainmenu.workout

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.adapters.ExercisesAdapter
import com.example.lukasz.galinski.gymprogressapp.dataclasses.ExerciseData
import com.example.lukasz.galinski.gymprogressapp.mainmenu.getDefaultHumanImage
import com.example.lukasz.galinski.gymprogressapp.mainmenu.setDefaultHumanImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import devs.mulham.horizontalcalendar.HorizontalCalendar
import devs.mulham.horizontalcalendar.HorizontalCalendarListener
import kotlinx.android.synthetic.main.exercises_layout.*
import kotlinx.android.synthetic.main.exercises_list_row.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private val groupNames = listOf("Barki","Klatka","Plecy","Triceps","Biceps","Grzbiet","Przedramiona","Brzuch","Pośladki","Uda","Łydki")
private const val FIREBASE_DATABASE_PATH = "workout"
private const val CALENDAR_START_END_DAY = 1
private const val EXERCISE_NAME_REF = "exerciseName"
private const val FIREBASE_SERIES_REFERENCE = "reference_series"
private const val DATE_PATTERN_REGEX = "dd-MM-yyyy"
private const val HUMAN_MODEL_MAN = "man_front"
private const val HUMAN_MODEL_WOMAN = "woman"
private lateinit var simple: SimpleDateFormat
var elementsCounter: Int = 0

class WorkoutExercise:AppCompatActivity() {
    private var isForward = true
    private var datePicked: String? = ""
    private var bodyPartButtons: ArrayList<Button> = arrayListOf()
    private lateinit var humanBodyView: List<Int>
    val listRecords : MutableList<ExerciseData?> = ArrayList()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercises_layout)
        bodyPartButtons = arrayListOf(Barki, Klatka, Plecy, Triceps, Biceps, Grzbiet, Przedramiona, Brzuch, Pośladki, Uda, Łydki)
        simple = SimpleDateFormat(DATE_PATTERN_REGEX, Locale.getDefault(Locale.Category.FORMAT))
        for (i in bodyPartButtons) {
            i.setOnClickListener(openAlertDialogWithPickedPart)
        }
        val userName = getCurrentUser()
        datePicked = SimpleDateFormat(DATE_PATTERN_REGEX, Locale.getDefault()).format(Date())

        listview.onItemClickListener = AdapterView.OnItemClickListener { _, view, _, _ ->
            val exerciseId = view?.series_number?.text.toString()
            val intent = Intent(applicationContext, SeriesExerciseActivity::class.java)
            val calendar = calendarView.horizontalCalendar.selectedDate
            val str: String = simple.format(calendar)
            val refString = "$FIREBASE_DATABASE_PATH/$str/$userName/$exerciseId"
            val sendNextArray = arrayListOf(refString, str)
            intent.putExtra(FIREBASE_SERIES_REFERENCE, sendNextArray)
            startActivity(intent)
        }
        loadDefaultHumanModel()
        updateSpinner()
        createHorizontalCalendar()
        loadExercises()
        model_default.setOnClickListener { selectModel() }
    }

    private fun loadDefaultHumanModel(){
        humanBodyView = getDefaultHumanImage(this)
        imageView.setImageDrawable(ContextCompat.getDrawable(this, humanBodyView[0]))
    }

    private fun getCurrentUser(): String{
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.currentUser.toString()
    }

    private fun loadExercises(){
        val ref = firebaseDatabase.reference.child(FIREBASE_DATABASE_PATH).child(datePicked.toString()).child(getCurrentUser())
        ref.addValueEventListener(object: ValueEventListener{
        override fun onDataChange(p0: DataSnapshot) {
            listRecords.clear()
            for (i in p0.children) {
                val exercise: ExerciseData? = i.getValue(
                    ExerciseData::class.java)
                elementsCounter = exercise?.exerciseId?.toInt() ?: 0
                if (exercise?.musclePartName == spinner.selectedItem.toString()) {
                    listRecords.add(exercise)
                }
            }
            val arrayAdapter = ExercisesAdapter(applicationContext, listRecords)
            listview.adapter = arrayAdapter
            customAdapterButtonsSet(arrayAdapter)
        }
        override fun onCancelled(p0: DatabaseError) {}
    })
}

    private fun createHorizontalCalendar(){
        val endDate: Calendar = Calendar.getInstance()
        endDate.add(Calendar.MONTH,
            CALENDAR_START_END_DAY
        )
        val startDate: Calendar = Calendar.getInstance()
        startDate.add(Calendar.MONTH, -CALENDAR_START_END_DAY)
        val horizontalCalendar = HorizontalCalendar
            .Builder(this,
                R.id.calendarView
            )
            .startDate(startDate.time)
            .endDate(endDate.time)
            .build()

        horizontalCalendar.calendarListener = (object: HorizontalCalendarListener() {
            override fun onDateSelected(date: Date, position: Int) {
                datePicked = simple.format(date)
                loadExercises()
            }
        })
    }

    private val openAlertDialogWithPickedPart = View.OnClickListener { v: View? ->
        val databaseReference = firebaseDatabase.getReference("$FIREBASE_DATABASE_PATH/$datePicked").child(getCurrentUser()).child(
            elementsCounter.inc().toString())
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.exercises_add_dialog, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val entryName = v?.resources?.getResourceEntryName(v.id)
        mDialogView.findViewById<TextView>(R.id.partName).text = entryName
        val alert = mBuilder.show()

        mDialogView.findViewById<Button>(R.id.dialogCancel_btn).setOnClickListener {
            alert.dismiss()
        }
        mDialogView.findViewById<Button>(R.id.dialogOk_btn).setOnClickListener {
            val exercise = ExerciseData(entryName.toString(),
                mDialogView.findViewById<EditText>(R.id.exercisename_editTxt).text.toString(), elementsCounter.inc().toLong())
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
            imageView.setImageResource(humanBodyView[1])
            isForward = false
        } else {
            if (!isForward) {
                imageView.setImageResource(humanBodyView[0])
                isForward = true
            }
        }
    }

    private fun selectModel(){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.model_choose, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val manCheckBox = mDialogView.findViewById<CheckBox>(R.id.man_checkbox)
        val womanCheckBox = mDialogView.findViewById<CheckBox>(R.id.woman_checkbox)
        val alert = mBuilder.show()
        val listener = CompoundButton.OnCheckedChangeListener { compound: CompoundButton, _: Boolean ->
            when (compound.id){
                R.id.man_checkbox -> {
                    setDefaultHumanImage(this, HUMAN_MODEL_MAN)
                }
                R.id.woman_checkbox -> {
                    setDefaultHumanImage(this, HUMAN_MODEL_WOMAN)
                }
            }
            loadDefaultHumanModel()
            alert.dismiss()
        }
        manCheckBox.setOnCheckedChangeListener(listener)
        womanCheckBox.setOnCheckedChangeListener(listener)
    }

    private fun updateSpinner(){
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, groupNames)
        spinner.adapter = arrayAdapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                loadExercises()
            }
        }
    }

    private fun customAdapterButtonsSet(arrayAdapter: ExercisesAdapter){
        arrayAdapter.setOnItemClickedListener(object: ListViewButtons{
            override fun onAddButtonPress() {}

            override fun onRemoveButtonPress(position: Int) {
                removeExerciseAlert(position.toString())
            }

            override fun onEditButtonPress(position: Int) {
                editExerciseAlert(position.toString())
            }
        })
    }

    private fun removeExerciseAlert(exerciseId: String){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.exercises_remove_alert, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val alert = mBuilder.show()

        mDialogView.findViewById<Button>(R.id.dialogCancel_btn).setOnClickListener {
            alert.dismiss()
        }
        mDialogView.findViewById<Button>(R.id.dialogOk_btn).setOnClickListener {
            firebaseDatabase.getReference("$FIREBASE_DATABASE_PATH/$datePicked/${getCurrentUser()}/$exerciseId").removeValue()
            alert.dismiss()
        }
    }

    private fun editExerciseAlert(exerciseId: String){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.exercises_edit_alert, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        val alert = mBuilder.show()

        val exerciseName = mDialogView.findViewById<EditText>(R.id.exercise_name)
        mDialogView.findViewById<Button>(R.id.dialogCancel_btn).setOnClickListener {
            alert.dismiss()
        }
        mDialogView.findViewById<Button>(R.id.dialogOk_btn).setOnClickListener {
            firebaseDatabase.getReference("$FIREBASE_DATABASE_PATH/$datePicked/${getCurrentUser()}/$exerciseId/$EXERCISE_NAME_REF")
                .setValue(exerciseName.text.toString())
            alert.dismiss()
        }
    }
}
