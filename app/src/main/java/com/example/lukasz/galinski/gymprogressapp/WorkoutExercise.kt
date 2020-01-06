package com.example.lukasz.galinski.gymprogressapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.exercises_add.*
import android.widget.ExpandableListAdapter


class WorkoutExercise:AppCompatActivity() {
    private var isForward = true
    private var datePicked: String? = ""
    private var bodyPartButtons: ArrayList<Button> = arrayListOf()

    val header : MutableList<ExerciseData?> = ArrayList()
    val numberList: MutableList<ExerciseData?> = ArrayList()
    val body : MutableList<MutableList<String>> = ArrayList()


    override fun onCreate(savedInstanceState: Bundle?) {
        datePicked = intent.getStringExtra("currentDate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.exercises_add)
        println("Data: $datePicked")
        bodyPartButtons =
            arrayListOf(barki, klata, Plecy, Triceps, biceps, Grzbiet, Przedramie, Brzuch, Uda)

        for (i in bodyPartButtons) {
            i.setOnClickListener(openAlertDialogWithPickedPart)
        }



    }

    private val openAlertDialogWithPickedPart = View.OnClickListener { v: View? ->
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.exercise_dialog, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialogView)
        mDialogView.findViewById<TextView>(R.id.partName).text =
            v?.resources?.getResourceEntryName(v.id)
        val alert = mBuilder.show()
        mDialogView.findViewById<Button>(R.id.dialogCancel_btn).setOnClickListener {
            alert.dismiss()
        }
        mDialogView.findViewById<Button>(R.id.dialogOk_btn).setOnClickListener {
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val databaseReference = database.getReference("users/username/$datePicked/")
            val exercise = ExerciseData(
                v?.resources?.getResourceEntryName(v.id).toString(),
                mDialogView.findViewById<EditText>(R.id.exercisename_editTxt).text.toString(),
                mDialogView.findViewById<EditText>(R.id.weight_editTxt).text.toString(),
                mDialogView.findViewById<EditText>(R.id.series_editTxt).text.toString(),
                mDialogView.findViewById<EditText>(R.id.reps_editTxt).text.toString()
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
        val listChild = HashMap<String,List<ExerciseData?>>()
        val listHeader = listOf("Barki","Klatka Piersiowa","Plecy","Triceps","Biceps","Grzbiet","Przedramie","Brzuch","Uda","łydki") //hardcode

        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        val ref = db.getReference("users/username/7_0_2020/klata")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                numberList.clear()
                header.clear()

                for (i in p0.children) {
                    val place = i.getValue(ExerciseData::class.java)

                    // test


                    //test
                    header.add(place)
                }

                 for (i in 0 until header.size){
                     numberList.add(header[i])
                 }



                listChild.put(listHeader[0], numberList)
                println("Finalna przekazwyana lista " + numberList)

                expandableListView.setAdapter(ExercisesAdapter(applicationContext, listHeader, listChild))

            }


            override fun onCancelled(p0: DatabaseError) {
                println("cancelled")
            }
        })
    }


}
