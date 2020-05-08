package com.example.lukasz.galinski.gymprogressapp.mainmenu

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.dataclasses.MeasuresData
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.common.internal.ShowFirstParty
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.chart_layout.*

private const val CHARTS_GRANULARITY = 1F
private const val DATASET_LINE_WIDTH = 3F
private const val DATASET_VALUE_TEXT_SIZE = 16F
private const val CHARTS_ANIMATION_SHOW_TIME = 1800
private const val MEASURES_REFERENCE = "measures"
val partNames = listOf("weight", "height", "chest", "waist", "hip", "arm", "thigh", "calf")
private val myData = ArrayList<MeasuresData?>()
private val entryList = ArrayList<Entry>()
private val dateList = ArrayList<String>()
class ChartsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_layout)
        val chart = findViewById<View>(R.id.line_chart_ez) as LineChart
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, partNames)
        partName_spinner.adapter = spinnerAdapter
        setChartData(chart)
    }

    private fun setChartData(chart: LineChart){
        var xAxisPosition = 0
        val reference = FirebaseDatabase.getInstance().reference.child("$MEASURES_REFERENCE/${getUserEmail()}/")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                dateList.clear(); myData.clear(); entryList.clear()
                for (data in p0.children) {
                    val exercise: MeasuresData? = data.getValue(MeasuresData::class.java)
                    val date = data.key.toString()
                    dateList.add(date)
                    myData.add(exercise)
                    entryList.add(Entry(xAxisPosition.toFloat(), exercise?.chest!!))
                    xAxisPosition++
                }
                setChartCustomStyle(chart)
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun setChartCustomStyle(chart: LineChart){
        val xAxis: XAxis = chart.xAxis
        val yAxis: YAxis = chart.axisLeft
        val customXAxisLabels = chartCustomXAxisLabel()
        xAxis.valueFormatter = customXAxisLabels

        val dataSet = LineDataSet(entryList, "Weight changes")
        xAxis.granularity = CHARTS_GRANULARITY
        dataSet.color = ContextCompat.getColor(this, R.color.colorAccent)
        dataSet.valueTextColor = ContextCompat.getColor(this, android.R.color.white)
        dataSet.lineWidth = DATASET_LINE_WIDTH
        dataSet.valueTextSize = DATASET_VALUE_TEXT_SIZE
        dataSet.setDrawFilled(true)
        dataSet.fillColor = ContextCompat.getColor(this, R.color.buttonColor)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textColor = ContextCompat.getColor(this, R.color.whiteColor)
        yAxis.textColor = ContextCompat.getColor(this, R.color.whiteColor)
        chart.animateX(CHARTS_ANIMATION_SHOW_TIME, Easing.EaseInExpo)
        chart.setBackgroundColor(ContextCompat.getColor(this, R.color.blackHalfTransparent))
        chart.setGridBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
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

    private fun getUserEmail(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        return user.toString()
    }
}


