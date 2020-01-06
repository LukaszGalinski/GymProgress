package com.example.lukasz.galinski.gymprogressapp

import android.content.Context
import android.graphics.Typeface
import android.util.ArrayMap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView


class ExercisesAdapter(val context: Context, val listOfHeaderData: List<String>, val listOfChildData: HashMap<String,List<ExerciseData?>>): BaseExpandableListAdapter() {
    var i=0
    override fun getGroup(position: Int): Any {
        return listOfHeaderData[position]
    }

    override fun isChildSelectable(headerPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
       val headerTitle = getGroup(groupPosition) as String
        val view: View = LayoutInflater.from(context).inflate(R.layout.group_layout,parent,false)
        val listHeaderText = view.findViewById<TextView>(R.id.group_text) as TextView

        listHeaderText.setTypeface(null,Typeface.BOLD)
        listHeaderText.text = headerTitle
        return view
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return listOfChildData[listOfHeaderData[groupPosition]]!!.size
    }

    override fun getChild(groupPosition: Int, childPosition: Int): ExerciseData?{
        return listOfChildData[listOfHeaderData[groupPosition]]!![childPosition]
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup?): View {

        val childText = getChild(groupPosition, childPosition)
        val view:View = LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        val itemName = view.findViewById<TextView>(R.id.exe_name) as TextView
        val itemWeight = view.findViewById<TextView>(R.id.weigth) as TextView
        val itemReps= view.findViewById<TextView>(R.id.repsik) as TextView
        val itemSeries= view.findViewById<TextView>(R.id.series) as TextView

        itemName.text = childText?.exerciseName
        itemWeight.text = childText?.weight
        itemReps.text = childText?.repsNumber
        itemSeries.text = childText?.seriesNumber

        return view
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun getGroupCount(): Int {
        return listOfHeaderData.size
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}