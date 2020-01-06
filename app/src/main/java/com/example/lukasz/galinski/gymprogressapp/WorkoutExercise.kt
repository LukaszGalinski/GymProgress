package com.example.lukasz.galinski.gymprogressapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.exercises_add.*
import org.w3c.dom.Text


class WorkoutExercise:AppCompatActivity() {
    private var isForward = true
    private var datePicked: String? = ""
    private var bodyPartButtons: ArrayList<Button> = arrayListOf()



    val groupNames = listOf("Barki","Klatka","Plecy","Triceps","Biceps","Grzbiet","Przedramiona","Brzuch","Pośladki","Uda","Łydki")
    val listRecords : MutableList<ExerciseData?> = ArrayList()
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        datePicked = intent.getStringExtra("currentDate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercises_add)
        bodyPartButtons =
            arrayListOf(Barki, Klatka, Plecy, Triceps, Biceps, Grzbiet, Przedramiona, Brzuch, Pośladki, Uda, Łydki)

        for (i in bodyPartButtons) {
            i.setOnClickListener(openAlertDialogWithPickedPart)
        }

        
        expandableListView.setOnChildClickListener { parent, _, groupPosition, childPosition, _ ->
            val elementid:ExerciseData = parent.expandableListAdapter.getChild(groupPosition,childPosition) as ExerciseData
            println("Twoje id: ${elementid.exerciseId}")

            true
        }
    }

    private val openAlertDialogWithPickedPart = View.OnClickListener { v: View? ->
        val databaseReference = firebaseDatabase.getReference("workout/username/$datePicked/").child(listRecords.size.toString())
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
                listRecords.size.toLong()
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

    override fun onStart() {
        super.onStart()
        val ref = firebaseDatabase.getReference("workout/username/$datePicked")
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
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("DatabaseReadingStatus: ", "Cancelled")
            }
        })
    }
}
