package com.example.labexam3.model

import java.time.LocalDate

data class Habit(
    val id: Long = System.currentTimeMillis(),
    val type: HabitType,
    var goal: Int,
    var currentValue: Int = 0,
    val date: String = LocalDate.now().toString()
)

enum class HabitType(val displayName: String, val unit: String) {
    STEPS("Steps", "steps"),
    WATER("Drink Water", "litres"),
    MEDITATION("Meditation", "minutes"),
    SLEEP("Sleep", "hours"),
    READING("Reading", "pages"),
    EXERCISE("Exercise", "minutes")
}
