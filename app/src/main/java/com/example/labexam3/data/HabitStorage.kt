package com.example.labexam3.data

import android.content.Context
import com.example.labexam3.model.Habit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HabitStorage(context: Context) {
    private val prefs = context.getSharedPreferences("habit_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val KEY = "habit_list"

    fun saveHabits(habits: List<Habit>) {
        prefs.edit().putString(KEY, gson.toJson(habits)).apply()
    }

    fun loadHabits(): MutableList<Habit> {
        val json = prefs.getString(KEY, null) ?: return mutableListOf()
        val type = object : TypeToken<List<Habit>>() {}.type
        return gson.fromJson<List<Habit>>(json, type).toMutableList()
    }
}
