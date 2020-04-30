package com.example.lukasz.galinski.gymprogressapp.mainmenu

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.adapters.SeriesAdapter
import com.example.lukasz.galinski.gymprogressapp.dataclasses.SeriesData
import com.example.lukasz.galinski.gymprogressapp.mainmenu.workout.listRecords
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.measures_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KProperty1

private const val DATE_PATTERN_REGEX = "dd-MM-yyyy"
private const val MEASURES_REFERENCE = "measures"
val editTextHints = arrayOf("weight", "height", "chest", "waist", "hip", "arm", "thigh", "calf")
private val editTextsArray = ArrayList<EditText>()
class MeasuresActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.measures_layout)
        val currentDay = SimpleDateFormat(DATE_PATTERN_REGEX, Locale.getDefault()).format(Date())
        getEditTextsFieldsIntoArray(editTextsArray)
        loadAllMeasures()
        loadMeasureFromToday(currentDay)

        measures_save.setOnClickListener {
            sendMeasuresToDatabase(editTextsArray, currentDay)
        }
    }

    private fun getEditTextsFieldsIntoArray(editTextsArray: ArrayList<EditText>){
        editTextsArray.clear()
        val linearLayout = findViewById<LinearLayout>(R.id.linear_edit)
        for (i in 0 until linearLayout.childCount) {
            val essa: EditText = linearLayout.getChildAt(i) as EditText
            editTextsArray.add(essa)
        }
    }

    private fun setEditTextsFields(editTextsArray: ArrayList<EditText>, dataElement: MeasuresData?){
        for (i in editTextsArray.indices) {
            val prop = editTextHints[i]
            val name: Float = readInstanceProperty(dataElement, prop)
            editTextsArray[i].setText(name.toString())
        }
    }

    private fun sendMeasuresToDatabase(editTextsArray: ArrayList<EditText>, currentDay: String){
        val dataArray = ArrayList<Float?>()
        for (i in editTextsArray.indices) {
            val fieldValue: Float? = editTextsArray[i].text.toString().toFloatOrNull() ?: 0F
            dataArray.add(fieldValue)
        }
        val dataSet =
            MeasuresData(
                dataArray[0], dataArray[1], dataArray[2], dataArray[3], dataArray[4], dataArray[5]
                , dataArray[6], dataArray[7]
            )
        FirebaseDatabase.getInstance().reference.child("$MEASURES_REFERENCE/${getCurrentUser()}/$currentDay").setValue(dataSet)
        clearEditTextsFields(editTextsArray)
        toast(resources.getString(R.string.data_save_success))
    }

    private fun clearEditTextsFields(editTextsArray: ArrayList<EditText>){
        for (i in editTextsArray.indices) {
            editTextsArray[i].text.clear()
        }
    }

    private fun loadMeasureFromToday(currentDay: String){
        val reference = FirebaseDatabase.getInstance().reference.child("$MEASURES_REFERENCE/${getCurrentUser()}/$currentDay")
        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val loadedData: MeasuresData? = p0.getValue(
                    MeasuresData::class.java)
                setEditTextsFields(editTextsArray, loadedData)
                toast(resources.getString(R.string.data_load_success))
            }

            override fun onCancelled(p0: DatabaseError) {
                toast(resources.getString(R.string.data_load_error))
            }
        })
    }


    private fun <R> readInstanceProperty(instance: MeasuresData?, propertyName: String): R {
        return if (instance != null){
            val property = instance::class.members.first { it.name == propertyName } as KProperty1<Any, *>
            property.get(instance) as R
        } else{
            0 as R
        }
    }

    private fun toast(text: String){
        android.widget.Toast.makeText(applicationContext, text, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentUser(): String{
        val firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.currentUser.toString()
    }

    private fun loadAllMeasures(){
        val reference = FirebaseDatabase.getInstance().reference.child("$MEASURES_REFERENCE/${getCurrentUser()}/")
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                for (data in p0.children){
                    val exercise: MeasuresData? = data.getValue(MeasuresData::class.java)
                    println("data key: " + data.key)
                    println("p0: " + exercise)

                }



             //   series_listview.layoutManager = LinearLayoutManager(this@SeriesExerciseActivity)
           //     val arrayAdapter = SeriesAdapter(listRecords)
            //    series_listview.adapter = arrayAdapter
            //    customAdapterButtonsSet(arrayAdapter)
            }

            override fun onCancelled(p0: DatabaseError) {
                toast(resources.getString(R.string.data_load_error))
            }
        })
    }
}
