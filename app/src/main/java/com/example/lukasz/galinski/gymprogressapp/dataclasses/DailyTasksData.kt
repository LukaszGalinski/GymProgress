package com.example.lukasz.galinski.gymprogressapp.dataclasses

data class DailyTasksData(
    var water: Float = 0f,
    val vegetables: Boolean = false,
    val fruit: Boolean = false,
    val gym: Boolean = false
)