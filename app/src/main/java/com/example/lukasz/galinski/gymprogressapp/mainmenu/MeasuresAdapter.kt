package com.example.lukasz.galinski.gymprogressapp.mainmenu

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lukasz.galinski.gymprogressapp.R
import kotlinx.android.synthetic.main.measures_row.view.*

class MeasuresAdapter(private val context: Context, private val seriesList: List<MeasuresData>, private val partNamesList: List<String>): RecyclerView.Adapter<MeasuresAdapter.MeasuresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasuresViewHolder {
        val singleItem = LayoutInflater.from(parent.context).inflate(R.layout.exercises_series_list_row, parent, false)
        return MeasuresViewHolder(singleItem)
    }

    override fun onBindViewHolder(holder: MeasuresViewHolder, position: Int) {
        val currentItem = seriesList[position]
        val currentLabel = partNamesList[position]
        holder.weight.text  = context.resources.getString(R.string.value_newline_kg, currentItem, currentLabel)
        holder.height.text  = context.resources.getString(R.string.value_newline_cm, currentItem, currentLabel)
        holder.chest.text  = context.resources.getString(R.string.value_newline_cm, currentItem, currentLabel)
        holder.waist.text  = context.resources.getString(R.string.value_newline_cm, currentItem, currentLabel)
        holder.hip.text  = context.resources.getString(R.string.value_newline_cm, currentItem, currentLabel)
        holder.arm.text  = context.resources.getString(R.string.value_newline_cm, currentItem, currentLabel)
        holder.thigh.text  = context.resources.getString(R.string.value_newline_cm, currentItem, currentLabel)
        holder.calf.text  = context.resources.getString(R.string.value_newline_cm, currentItem, currentLabel)

    }

    class MeasuresViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val weight: TextView = itemView.grid_weight
        val height: TextView = itemView.grid_height
        val chest: TextView = itemView.grid_chest
        val waist: TextView = itemView.grid_waist
        val hip: TextView = itemView.grid_hip
        val arm: TextView = itemView.grid_arm
        val thigh: TextView = itemView.grid_thigh
        val calf: TextView = itemView.grid_calf

    }

    override fun getItemCount(): Int {
        return seriesList.size
    }
}