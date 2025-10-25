package com.example.labexam3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labexam3.R
import com.example.labexam3.model.Habit

class HabitAdapter(
    private val habits: List<Habit>,
    private val listener: (Habit?, Boolean) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_HABIT = 0
    private val TYPE_ADD = 1

    override fun getItemViewType(position: Int): Int {
        return if (position < habits.size) TYPE_HABIT else TYPE_ADD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HABIT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_habit, parent, false)
            HabitViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_habit, parent, false)
            AddViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HabitViewHolder && position < habits.size) {
            val habit = habits[position]
            holder.bind(habit)
            holder.itemView.setOnClickListener { listener(habit, false) }
        } else if (holder is AddViewHolder) {
            holder.itemView.setOnClickListener { listener(null, true) }
        }
    }

    override fun getItemCount(): Int {
        return if (habits.size < 6) habits.size + 1 else habits.size
    }


    inner class HabitViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon: ImageView = itemView.findViewById(R.id.habitIcon)
        private val title: TextView = itemView.findViewById(R.id.habitTitle)
        private val goal: TextView = itemView.findViewById(R.id.habitGoal)
        private val progress: ProgressBar = itemView.findViewById(R.id.habitProgress)

        fun bind(habit: Habit) {
            title.text = habit.type.displayName
            goal.text = "${habit.currentValue}/${habit.goal} ${habit.type.unit}"

            progress.max = habit.goal
            progress.progress = habit.currentValue

            when (habit.type) {
                com.example.labexam3.model.HabitType.STEPS -> icon.setImageResource(R.drawable.ic_walk)
                com.example.labexam3.model.HabitType.WATER -> icon.setImageResource(R.drawable.ic_water)
                com.example.labexam3.model.HabitType.MEDITATION -> icon.setImageResource(R.drawable.ic_habits)
                com.example.labexam3.model.HabitType.SLEEP -> icon.setImageResource(R.drawable.ic_alarm)
                com.example.labexam3.model.HabitType.READING -> icon.setImageResource(R.drawable.ic_book)
                com.example.labexam3.model.HabitType.EXERCISE -> icon.setImageResource(R.drawable.ic_exercise)
            }
        }
    }

    inner class AddViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
