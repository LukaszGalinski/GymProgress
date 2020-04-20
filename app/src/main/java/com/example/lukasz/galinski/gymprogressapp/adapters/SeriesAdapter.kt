package com.example.lukasz.galinski.gymprogressapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.dataclasses.SeriesData
import com.example.lukasz.galinski.gymprogressapp.mainmenu.workout.ListViewButtons


class SeriesAdapter(private val context: Context, private val array: List<SeriesData?>): BaseAdapter() {
    private lateinit var listener: ListViewButtons

    fun setOnItemClickedListener(listener: ListViewButtons){
        this.listener = listener
    }

    override fun getItem(position: Int): SeriesData? {
        return array[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return array.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val layoutInflater = LayoutInflater.from(context)
        val mainRow = layoutInflater.inflate(R.layout.exercises_series_list_row, parent, false)
        val id = mainRow.findViewById<TextView>(R.id.series_id)
        val reps = mainRow.findViewById<EditText>(R.id.reps)
        val weight = mainRow.findViewById<EditText>(R.id.weight)
        val addBtn = mainRow.findViewById<ImageButton>(R.id.addSeries)
        val removeBtn = mainRow.findViewById<ImageButton>(R.id.removeButton)
        id.text = (getItemId(position) + 1).toString()
        reps.setText(array[position]?.repsNumber.toString())
        weight.setText(array[position]?.weight.toString())

        if (position == array.size-1){
            addBtn.visibility = View.VISIBLE
        }

        addBtn.setOnClickListener {
            println("id: " + id.text)
            listener.onAddButtonPress(id.text.toString().toLong(), weight.text.toString(), reps.text.toString())
          //  listener.saveNewData()
        }

        removeBtn.setOnClickListener {
            println("position" + position )
      //      listener.saveNewData()
        //    listener.onRemoveButtonPress(getItemId(position))
        }
        return mainRow
    }

    private fun getDataFromList(reps: EditText, weight: EditText): MutableList<SeriesData?>{
        val dataFromList : MutableList<SeriesData?> = ArrayList()
        for (i in array.indices) {
            dataFromList.add(getItem(i))
            println("nowa data: " + SeriesData(getItemId(i).toInt(), reps.text.toString().toInt(), weight.text.toString().toFloat()))
        }

        println("newdata " + dataFromList)
        return dataFromList
    }
}