package com.example.lukasz.galinski.gymprogressapp.mainmenu.measures

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.adapters.MeasuresAdapter
import com.example.lukasz.galinski.gymprogressapp.dataclasses.MeasuresData
import com.example.lukasz.galinski.gymprogressapp.loginfeatures.getCurrentUser
import com.example.lukasz.galinski.gymprogressapp.mainmenu.getDefaultHumanImage
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.measures_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KProperty1

private const val DATE_PATTERN = "dd-MM-yyyy"
private const val DOUBLE_DIGIT_FORMAT = "%02d"
private const val MEASURES_REFERENCE = "measures"
val editTextHints = listOf("weight", "height", "chest", "waist", "hip", "arm", "thigh", "calf")
private val editTextsArray = ArrayList<EditText>()
private val dateList = ArrayList<String>()
private val measuresList = ArrayList<MeasuresData?>()
private lateinit var measuresAdapter: MeasuresAdapter
class MeasuresActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.measures_layout)
        val currentDay = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).format(Date())
        loadEditTextsFieldsIntoArray(editTextsArray)
        loadAllMeasures()
        loadDefaultHumanModel()
        loadMeasureFromTodayIntoEditTexts(currentDay)

        measures_save.setOnClickListener {
            sendMeasuresToDatabase(editTextsArray, currentDay)
        }

        filter_results.setOnClickListener {
            openFilterAlertWithCalendarView()
        }

        reset_filters.setOnClickListener {
            loadAllMeasures()
        }
    }

    private fun loadAllMeasures(){
        val reference = FirebaseDatabase.getInstance().reference.child("$MEASURES_REFERENCE/${getCurrentUser()}/")
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                measuresList.clear()
                dateList.clear()
                for (data in p0.children){
                    val exercise: MeasuresData? = data.getValue(MeasuresData::class.java)
                    val date = data.key.toString()
                    measuresList.add(exercise)
                    dateList.add(date)
                }
                recycler_view_measures.layoutManager = LinearLayoutManager(this@MeasuresActivity)
                measuresAdapter = MeasuresAdapter(this@MeasuresActivity, measuresList, editTextHints, dateList)
                recycler_view_measures.adapter = measuresAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                toast(resources.getString(R.string.data_load_error))
            }
        })
    }

    private fun loadDefaultHumanModel(){
        val humanBodyView = getDefaultHumanImage(this)
        imageView4.setImageDrawable(ContextCompat.getDrawable(this, humanBodyView[0]))
    }

    private fun loadEditTextsFieldsIntoArray(editTextsArray: ArrayList<EditText>){
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

    private fun loadMeasureFromTodayIntoEditTexts(currentDay: String){
        val reference = FirebaseDatabase.getInstance().reference.child("$MEASURES_REFERENCE/${getCurrentUser()}/$currentDay")
        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val loadedData: MeasuresData? = p0.getValue(MeasuresData::class.java)
                setEditTextsFields(editTextsArray, loadedData)
                toast(resources.getString(R.string.data_load_success))
            }

            override fun onCancelled(p0: DatabaseError) {
                toast(resources.getString(R.string.data_load_error))
            }
        })
    }

    private fun openFilterAlertWithCalendarView(){
        val mDialogView = LayoutInflater.from(this@MeasuresActivity).inflate(R.layout.measures_calendar_alert, null)
        val mBuilder = AlertDialog.Builder(this@MeasuresActivity).setView(mDialogView)

        val positiveButton = mDialogView.findViewById<Button>(R.id.measures_next)
        val cancelButton = mDialogView.findViewById<Button>(R.id.measures_cancel)
        val calendar = mDialogView.findViewById<CalendarView>(R.id.calendarView2)

        val simple = SimpleDateFormat(DATE_PATTERN, Locale.getDefault(Locale.Category.FORMAT))
        var date = simple.format(calendar.date)

        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val monthVal = String.format(DOUBLE_DIGIT_FORMAT, month+1)
            val day = String.format(DOUBLE_DIGIT_FORMAT, dayOfMonth)
            date = "$day-$monthVal-$year"
        }
        val alert = mBuilder.show()
        positiveButton.setOnClickListener {
            loadMeasureFromSpecificDate(date)
            alert.dismiss()
        }

        cancelButton.setOnClickListener {
            alert.dismiss()
        }
    }

    private fun loadMeasureFromSpecificDate(specificDate: String){
        measuresList.clear()
        dateList.clear()
        val reference = FirebaseDatabase.getInstance().reference.child("$MEASURES_REFERENCE/${getCurrentUser()}/$specificDate")
        reference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val loadedData: MeasuresData? = p0.getValue(
                    MeasuresData::class.java)
                val date = p0.key.toString()
                measuresList.add(loadedData)
                dateList.add(date)
                measuresAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {
                toast(resources.getString(R.string.data_load_error))
            }
        })
    }

    @Suppress("UNCHECKED_CAST")
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
}
