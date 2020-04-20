package com.example.lukasz.galinski.gymprogressapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.dataclasses.SeriesData
import com.example.lukasz.galinski.gymprogressapp.mainmenu.workout.OnItemClicked


class SeriesAdapter(private val context: Context, private val array: List<SeriesData?>): BaseAdapter() {
    private lateinit var listener: OnItemClicked

    fun setOnItemClickedListener(listener: OnItemClicked){
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
        val button = mainRow.findViewById<ImageButton>(R.id.addSeries)
        id.text = (getItemId(position) + 1).toString()
        reps.setText(array[position]?.repsNumber.toString())
        weight.setText(array[position]?.weight.toString())

        if (position == array.size-1){
            button.visibility = View.VISIBLE
        }

        button.setOnClickListener {
            listener.onItemClicked()
        }
        return mainRow
    }
}