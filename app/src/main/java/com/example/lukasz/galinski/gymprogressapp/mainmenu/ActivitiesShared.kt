package com.example.lukasz.galinski.gymprogressapp.mainmenu

import android.content.Context
import com.example.lukasz.galinski.gymprogressapp.R

private const val HUMAN_MODEL_OBJECT = "human_type"
private const val HUMAN_MODEL_MAN = "man"
private const val HUMAN_MODEL_WOMAN = "woman"
private const val DRAWABLE_MAN = R.drawable.man
private const val DRAWABLE_WOMAN = R.drawable.hantel

fun setDefaultHumanImage(context: Context, humanBodyType: String){
    val sharedPreferences  = context.getSharedPreferences(HUMAN_MODEL_OBJECT, Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString(HUMAN_MODEL_OBJECT, humanBodyType)
    editor.apply()
}

fun getDefaultHumanImage(context: Context){
    val sharedPreferences  = context.getSharedPreferences(HUMAN_MODEL_OBJECT, Context.MODE_PRIVATE)
    val variable = sharedPreferences.getString(HUMAN_MODEL_OBJECT, HUMAN_MODEL_MAN)
    humanImageSetter(variable.toString())
}

fun humanImageSetter(humanType: String): Int {
    var model = DRAWABLE_MAN
    when (humanType){
        HUMAN_MODEL_MAN -> {
            model = DRAWABLE_MAN
        }
        HUMAN_MODEL_WOMAN -> {
            model = DRAWABLE_WOMAN
        }
    }
    return model
}