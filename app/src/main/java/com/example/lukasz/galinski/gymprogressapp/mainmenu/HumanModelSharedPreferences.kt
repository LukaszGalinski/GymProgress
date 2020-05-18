package com.example.lukasz.galinski.gymprogressapp.mainmenu

import android.content.Context
import com.example.lukasz.galinski.gymprogressapp.R

private const val HUMAN_MODEL_OBJECT = "human_type"
private const val HUMAN_MODEL_MAN = "man_front"
private const val HUMAN_MODEL_WOMAN = "woman"
private const val DRAWABLE_MAN_FRONT = R.drawable.man_front
private const val DRAWABLE_MAN_BACK = R.drawable.man_back
private const val DRAWABLE_WOMAN_FRONT = R.drawable.woman_front
private const val DRAWABLE_WOMAN_BACK = R.drawable.woman_back

fun setDefaultHumanImage(context: Context, humanBodyType: String){
    val sharedPreferences  = context.getSharedPreferences(HUMAN_MODEL_OBJECT, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(HUMAN_MODEL_OBJECT, humanBodyType)
    editor.apply()
}

fun getDefaultHumanImage(context: Context): List<Int> {
    val sharedPreferences  = context.getSharedPreferences(HUMAN_MODEL_OBJECT, Context.MODE_PRIVATE)
    val variable = sharedPreferences.getString(HUMAN_MODEL_OBJECT, HUMAN_MODEL_MAN)
    return humanImageSetter(variable.toString())
}

fun humanImageSetter(humanType: String): List<Int> {
    var modelFront = DRAWABLE_MAN_FRONT
    var modelBack = DRAWABLE_MAN_BACK
    when (humanType){
        HUMAN_MODEL_MAN -> {
            modelFront = DRAWABLE_MAN_FRONT
            modelBack = DRAWABLE_MAN_BACK
        }
        HUMAN_MODEL_WOMAN -> {
            modelFront = DRAWABLE_WOMAN_FRONT
            modelBack = DRAWABLE_WOMAN_BACK
        }
    }
    return listOf(modelFront, modelBack)
}