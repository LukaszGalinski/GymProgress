package com.example.lukasz.galinski.gymprogressapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
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

       setAddButtonVisibleOnTheLastElement(holder.addBtn, position)
    }

    class SeriesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val seriesId: TextView = itemView.series_id
        val reps: TextView = itemView.reps
        val weight: TextView = itemView.weight
        val addBtn: ImageButton = itemView.addSeries
        val removeBtn: ImageButton = itemView.removeButton
        val editBtn: ImageButton = itemView.editButton
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }

    private fun setAddButtonVisibleOnTheLastElement(button: ImageButton, position: Int){
        if (position != seriesList.size-1){
            button.visibility = View.GONE
        }else{
            button.visibility = View.VISIBLE
        }
    }
}