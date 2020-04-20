package com.example.lukasz.galinski.gymprogressapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.dataclasses.ExerciseData

class ExercisesAdapter(private val context: Context, private val array: List<ExerciseData?>): BaseAdapter() {

    override fun getItem(position: Int): ExerciseData? {
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
        val mainRow = layoutInflater.inflate(R.layout.exercises_list_row, parent, false)
        val name = mainRow.findViewById<TextView>(R.id.exercise_name)
        val index = mainRow.findViewById<TextView>(R.id.series_number)
        name.text = array[position]?.exerciseName
        index.text = array[position]?.exerciseId.toString()

        return mainRow
    }
}