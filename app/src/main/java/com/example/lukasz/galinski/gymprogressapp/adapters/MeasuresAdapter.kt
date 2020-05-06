package com.example.lukasz.galinski.gymprogressapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lukasz.galinski.gymprogressapp.R
import com.example.lukasz.galinski.gymprogressapp.dataclasses.MeasuresData
import kotlinx.android.synthetic.main.measures_row.view.*

class MeasuresAdapter(private val context: Context, private val seriesList: List<MeasuresData?>, private val partNamesList: List<String>, private val dateList: List<String>): RecyclerView.Adapter<MeasuresAdapter.MeasuresViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeasuresViewHolder {
        val singleItem = LayoutInflater.from(parent.context).inflate(R.layout.measures_row, parent, false)
        return MeasuresViewHolder(
            singleItem
        )
    }

    override fun onBindViewHolder(holder: MeasuresViewHolder, position: Int) {
        val currentItem = seriesList[position]
        val currentLabel = partNamesList
        val currentDate = dateList[position]

        if (currentItem == null){
            holder.grid.visibility = View.GONE
            holder.divideLine.visibility = View.GONE
            holder.date.text = context.resources.getString(R.string.no_matching_results, currentDate)

        } else{
            holder.grid.visibility = View.VISIBLE
            holder.divideLine.visibility = View.VISIBLE
            holder.date.text = currentDate
            holder.weight.text  = context.resources.getString(R.string.value_newline_kg, currentItem.weight.toString(), currentLabel[0])
            holder.height.text  = context.resources.getString(R.string.value_newline_cm, currentItem.height.toString(), currentLabel[1])
            holder.chest.text  = context.resources.getString(R.string.value_newline_cm, currentItem.chest.toString(), currentLabel[2])
            holder.waist.text  = context.resources.getString(R.string.value_newline_cm, currentItem.waist.toString(), currentLabel[3])
            holder.hip.text  = context.resources.getString(R.string.value_newline_cm, currentItem.hip.toString(), currentLabel[4])
            holder.arm.text  = context.resources.getString(R.string.value_newline_cm, currentItem.arm.toString(), currentLabel[5])
            holder.thigh.text  = context.resources.getString(R.string.value_newline_cm, currentItem.thigh.toString(), currentLabel[6])
            holder.calf.text  = context.resources.getString(R.string.value_newline_cm, currentItem.calf.toString(), currentLabel[7])
        }
    }

    class MeasuresViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val date: TextView = itemView.grid_date
        val weight: TextView = itemView.grid_weight
        val height: TextView = itemView.grid_height
        val chest: TextView = itemView.grid_chest
        val waist: TextView = itemView.grid_waist
        val hip: TextView = itemView.grid_hip
        val arm: TextView = itemView.grid_arm
        val thigh: TextView = itemView.grid_thigh
        val calf: TextView = itemView.grid_calf
        val grid: GridLayout = itemView.gridlayout_custom
        val divideLine: View = itemView.divide_line
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }
}