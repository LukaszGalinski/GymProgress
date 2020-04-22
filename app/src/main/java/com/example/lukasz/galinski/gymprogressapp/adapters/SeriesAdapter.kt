package com.example.lukasz.galinski.gymprogressapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.dataclasses.SeriesData
import com.example.lukasz.galinski.gymprogressapp.mainmenu.workout.ListViewButtons
import kotlinx.android.synthetic.main.exercises_series_list_row.view.*

class SeriesAdapter(private val seriesList: List<SeriesData?>): RecyclerView.Adapter<SeriesAdapter.SeriesViewHolder>() {
    private lateinit var listener: ListViewButtons

    fun setOnItemClickedListener(listener: ListViewButtons){
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesViewHolder {
        val singleItem = LayoutInflater.from(parent.context).inflate(R.layout.exercises_series_list_row, parent, false)
        return SeriesViewHolder(singleItem)
    }

    override fun onBindViewHolder(holder: SeriesViewHolder, position: Int) {
        val currentItem = seriesList[position]

        holder.seriesId.text = position.inc().toString()
        holder.reps.text = currentItem?.repsNumber.toString()
        holder.weight.text = currentItem?.weight.toString()

        holder.addBtn.setOnClickListener {
            listener.onAddButtonPress()
        }

        holder.editBtn.setOnClickListener {
            listener.onEditButtonPress(position)
        }

        holder.removeBtn.setOnClickListener {
            listener.onRemoveButtonPress(position)
        }

        //seperate
        if (position != seriesList.size-1){
            holder.addBtn.visibility = View.GONE
        }else{
            holder.addBtn.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }

    class SeriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val seriesId: TextView = itemView.series_id
        val reps: TextView = itemView.reps
        val weight: TextView = itemView.weight
        val addBtn = itemView.addSeries
        val removeBtn = itemView.removeButton
        val editBtn = itemView.editButton
    }


/*
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

        id.text = (getItemId(position) + 1).toString()
        reps.setText(array[position]?.repsNumber.toString())
        weight.setText(array[position]?.weight.toString())



        addBtn.setOnClickListener {
            println("dodano item ")
            listener.onAddButtonPress()

        }

        removeBtn.setOnClickListener {
            println("removed" + position )

        }

        editBtn.setOnClickListener {
            println("edited: "+ id.text.toString().toLong())
            listener.onEditButtonPress(id.text.toString().toLong())
        }
        return mainRow
    }
*/


}