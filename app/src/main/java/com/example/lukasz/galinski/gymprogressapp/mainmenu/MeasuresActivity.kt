package com.example.lukasz.galinski.gymprogressapp.mainmenu

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.dataclasses.MeasuresData
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
private const val MEASURES_REFERENCE = "Measures/null/29-04-2020/"
private val editTextsArray = ArrayList<EditText>()
class MeasuresActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.measures_layout)
        val currentDay = SimpleDateFormat(DATE_PATTERN_REGEX, Locale.getDefault()).format(Date())
        getEditTextsFieldsIntoArray(editTextsArray)
        loadMeasuresFromDatabase()

        measures_save.setOnClickListener {
            sendMeasuresToDatabase(editTextsArray, currentDay)
        }
    }

    private fun getEditTextsFieldsIntoArray(editTextsArray: ArrayList<EditText>){
        val linearLayout = findViewById<LinearLayout>(R.id.linear_edit)
        for (i in 0 until linearLayout.childCount) {
            val essa: EditText = linearLayout.getChildAt(i) as EditText
            editTextsArray.add(essa)
        }
    }


    private fun setEditTextsFields(editTextsArray: ArrayList<EditText>, dataElement: MeasuresData?){
        val array = arrayOf("weight", "height", "chest", "waist", "hip", "arm", "thigh", "calf")
        for (i in editTextsArray.indices) {
         //   println("Twoje i: $i")
            val prop = array[i]
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

        val dataSet = MeasuresData(dataArray[0], dataArray[1], dataArray[2], dataArray[3], dataArray[4], dataArray[5]
            , dataArray[6], dataArray[7])
        FirebaseDatabase.getInstance().reference.child(MEASURES_REFERENCE).setValue(dataSet)
        clearEditTextsFields(editTextsArray)
    }

    private fun clearEditTextsFields(editTextsArray: ArrayList<EditText>){
        for (i in editTextsArray.indices) {
            editTextsArray[i].text.clear()
        }
    }

    private fun loadMeasuresFromDatabase(){
        val reference = FirebaseDatabase.getInstance().reference.child(MEASURES_REFERENCE)

        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val loadedData: MeasuresData? = p0.getValue(MeasuresData::class.java)
                setEditTextsFields(editTextsArray, loadedData)

            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    @Suppress("UNCHECKED_CAST")
    fun <R> readInstanceProperty(instance: MeasuresData?, propertyName: String): R {
        val property = instance!!::class.members
            .first { it.name == propertyName } as KProperty1<Any, *>
        return property.get(instance) as R
    }
}
