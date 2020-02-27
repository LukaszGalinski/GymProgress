package com.example.lukasz.galinski.gymprogressapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.exercises_add.*

private val groupNames = listOf("Barki","Klatka","Plecy","Triceps","Biceps","Grzbiet","Przedramiona","Brzuch","Pośladki","Uda","Łydki")
private const val LISTVIEW_HEIGHT = 250
private const val CURRENT_DATE = "currentDate"
private const val FIREBASE_DATABASE_PATH = "workout/username/"
private const val LOG = "Log"
private const val LOG_CANCELINFO = "Cancelled"

class WorkoutExercise:AppCompatActivity() {
    private var isForward = true
    private var datePicked: String? = ""
    private var lastIndexExerciseId: Long? = 0
    private var bodyPartButtons: ArrayList<Button> = arrayListOf()
    val listRecords : MutableList<ExerciseData?> = ArrayList()

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        datePicked = intent.getStringExtra(CURRENT_DATE)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercises_add)
        bodyPartButtons = arrayListOf(Barki, Klatka, Plecy, Triceps, Biceps, Grzbiet, Przedramiona, Brzuch, Pośladki, Uda, Łydki)

        for (i in bodyPartButtons) {
            i.setOnClickListener(openAlertDialogWithPickedPart)
        }
        expandableListView.setOnChildClickListener { parent, view, groupPosition, childPosition, _ ->
            val chosenChildExerciseData:ExerciseData = parent.expandableListAdapter.getChild(groupPosition,childPosition) as ExerciseData
            val popupMenu = PopupMenu(applicationContext,view)
            popupMenu.menuInflater.inflate(R.menu.menu_main, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item: MenuItem? ->
                when (item?.itemId){
                    R.id.edit_thing ->
                        editElement(chosenChildExerciseData)
                    R.id.delete_thing ->
                        deleteElement(chosenChildExerciseData)
                }
                true
            }
            popupMenu.show()
            true
        }
    }

    override fun onStart() {
        super.onStart()
        val ref = firebaseDatabase.getReference(FIREBASE_DATABASE_PATH + datePicked)
        val listChild = HashMap<String,List<ExerciseData?>>()
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                listRecords.clear()
                for (i in p0.children) {
                    val place = i.getValue(ExerciseData::class.java)
                    listRecords.add(place)
                }
                for (partElement in groupNames.indices) {
                    listChild[groupNames[partElement]] = listRecords.filter { it?.musclePartName == groupNames[partElement]}
                }
                expandableListView.setAdapter(ExercisesAdapter(applicationContext, groupNames, listChild))
                if (listRecords.isNotEmpty()) lastIndexExerciseId = listRecords[listRecords.lastIndex]?.exerciseId
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d(LOG, LOG_CANCELINFO)
            }
        })
        expandableListView.setGroupIndicator(resources.getDrawable(R.drawable.indicator_cfg))
        expandableListView.setOnGroupClickListener{ parent: ExpandableListView, _: View, i: Int, _: Long ->
            val adapter : ExpandableListAdapter = parent.expandableListAdapter
            val recordsNumber = adapter.getChildrenCount(i)
            val height = expandableListView.height
            if (expandableListView.isGroupExpanded(i)){
                expandableListView.collapseGroup(i)
                expandableListView.layoutParams.height = height - (recordsNumber*LISTVIEW_HEIGHT)
            }
            else{
                expandableListView.expandGroup(i)
                expandableListView.layoutParams.height = height + (recordsNumber*LISTVIEW_HEIGHT)
            }
            return@setOnGroupClickListener true

        }
    }
    private val openAlertDialogWithPickedPart = View.OnClickListener { v: View? ->
        val databaseReference = firebaseDatabase.getReference(FIREBASE_DATABASE_PATH + datePicked).child(lastIndexExerciseId?.inc().toString())
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
                lastIndexExerciseId?.inc()
            )
            databaseReference.setValue(exercise)
            alert.dismiss()
        }
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
}
