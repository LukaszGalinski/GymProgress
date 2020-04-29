package com.example.lukasz.galinski.gymprogressapp.mainmenu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lukasz.galinski.gymprogressapp.R
import kotlinx.android.synthetic.main.measures_row.view.*

class MeasureAdapter(private val seriesList: List<String>): RecyclerView.Adapter<MeasureAdapter.DefViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefViewHolder {
        val singleItem = LayoutInflater.from(parent.context).inflate(R.layout.measures_row, parent, false)
        return DefViewHolder(singleItem)
    }

    override fun onBindViewHolder(holder: DefViewHolder, position: Int) {
        holder.item.text = seriesList[position]
    }

    class DefViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val item = itemView.texthold
    }

    override fun getItemCount(): Int {
        return seriesList.size
    }
}