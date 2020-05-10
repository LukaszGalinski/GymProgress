package com.example.lukasz.galinski.gymprogressapp.mainmenu

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lukasz.galinski.gymprogressapp.R
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.chart_layout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val CHARTS_GRANULARITY = 1F
private const val DATA_SET_LINE_WIDTH = 3F
private const val DATA_SET_VALUE_TEXT_SIZE = 16F
private const val CHARTS_ANIMATION_SHOW_TIME = 1800
private const val TIME_PERIOD_WEEK = 6
private const val TIME_PERIOD_MONTH = 29
private const val MEASURES_REFERENCE = "measures"
private const val ONE_DIGIT_AFTER_DOT_PATTERN = "%.1f"
private const val DATE_ONLY_FORMAT = "dd-MM-yyyy"
val partNames = listOf("weight", "height", "chest", "waist", "hip", "arm", "thigh", "calf")
private val myDataSet = ArrayList<Float?>()
private val entryList = ArrayList<Entry>()
private val dateList = ArrayList<String>()
private var timePeriod = TIME_PERIOD_WEEK
private var partName: String = ""
class ChartsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_layout)
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, partNames)
        partName_spinner.adapter = spinnerAdapter
        partName_spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                partName = partName_spinner.getItemAtPosition(position).toString()
                setChartData(partName)
            }
        }
    }

    private fun setChartData(partName: String){
        val chart = findViewById<View>(R.id.line_chart_ez) as LineChart
        dateList.clear(); myDataSet.clear(); entryList.clear()
        var xAxisPosition = 0
        val reference = FirebaseDatabase.getInstance().reference.child("$MEASURES_REFERENCE/${getUserEmail()}/")
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (i in timePeriod downTo 0) {
                    val temporaryDate = getCalculatedDate(-i)
                    val exercise = p0.child("$temporaryDate/$partName").value ?: 0
                    dateList.add(temporaryDate.toString())
                    myDataSet.add(exercise.toString().toFloat())
                    entryList.add(Entry(xAxisPosition.toFloat(), myDataSet[xAxisPosition]!!))
                    xAxisPosition++
                }
                setChartCustomStyle(chart, partName)
                findMinMaxAverageValues(myDataSet)
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
        }

    private fun setChartCustomStyle(chart: LineChart, partName: String){
        val xAxis: XAxis = chart.xAxis
        val yAxis: YAxis = chart.axisLeft
        val customXAxisLabels = chartCustomXAxisLabel()
        xAxis.valueFormatter = customXAxisLabels

        val dataSet = LineDataSet(entryList, partName)
        xAxis.granularity = CHARTS_GRANULARITY
        dataSet.color = ContextCompat.getColor(this, R.color.colorAccent)
        dataSet.valueTextColor = ContextCompat.getColor(this, android.R.color.white)
        dataSet.lineWidth = DATA_SET_LINE_WIDTH
        dataSet.valueTextSize = DATA_SET_VALUE_TEXT_SIZE
        dataSet.setDrawFilled(true)
        dataSet.fillColor = ContextCompat.getColor(this, R.color.buttonColor)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = ContextCompat.getColor(this, R.color.whiteColor)
        yAxis.textColor = ContextCompat.getColor(this, R.color.whiteColor)
        chart.animateX(CHARTS_ANIMATION_SHOW_TIME, Easing.EaseInExpo)
        chart.setBackgroundColor(ContextCompat.getColor(this, R.color.blackHalfTransparent))
        chart.setGridBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
        chart.setTouchEnabled(true)
        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.invalidate()
    }

    private fun chartCustomXAxisLabel(): ValueFormatter{
        return object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase): String {
                return dateList.getOrNull(value.toInt()) ?: value.toString()
            }
        }
    }

    private fun getCalculatedDate(days: Int): String? {
        val cal = Calendar.getInstance()
        val s = SimpleDateFormat(DATE_ONLY_FORMAT, Locale.getDefault())
        cal.add(Calendar.DAY_OF_YEAR, days)
        return s.format(Date(cal.timeInMillis))
    }

    private fun getUserEmail(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        return user.toString()
    }

    fun changeTimePeriodOnTheChart(v: View){
        when (v.id){
            R.id.week_button -> timePeriod = TIME_PERIOD_WEEK
            R.id.month_button -> timePeriod = TIME_PERIOD_MONTH
        }
        setChartData(partName)
    }

    private fun findMinMaxAverageValues(myDataSet: ArrayList<Float?>) {
        val clearValuesOnly = ArrayList<Float>()
        for (i in myDataSet.indices) {
            if (myDataSet[i] != 0F) {
                clearValuesOnly.add(myDataSet[i]!!)
            }
        }
        if (clearValuesOnly.isNotEmpty()) {
            var minim = clearValuesOnly[0]
            var maximum = clearValuesOnly[0]
            var avg = 0F
            for (i in clearValuesOnly.indices) {
                if (clearValuesOnly[i] < minim) {
                    minim = clearValuesOnly[i]
                }
                if (clearValuesOnly[i] > maximum) {
                    maximum = clearValuesOnly[i]
                }
                avg += clearValuesOnly[i]
            }
            lowest_value.text = resources.getString(R.string.value_lowest, minim.toString())
            highest_value.text = resources.getString(R.string.value_highest, maximum.toString())
            average_value.text = resources.getString(
                R.string.value_average,
                String.format(ONE_DIGIT_AFTER_DOT_PATTERN, avg / clearValuesOnly.size)
            )
        }
    }
}


