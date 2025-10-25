package com.example.labexam3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.labexam3.data.HabitStorage
import com.example.labexam3.model.Habit
import com.example.labexam3.model.HabitType

class HabitDetailsActivity : AppCompatActivity() {

    private lateinit var storage: HabitStorage
    private var habitId: Long = -1
    private var habit: Habit? = null

    private lateinit var title: TextView
    private lateinit var goalInput: EditText
    private lateinit var currentInput: EditText
    private lateinit var updateBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var backBtn: Button
    private lateinit var topImage: ImageView
    private lateinit var goalLabel: TextView
    private lateinit var currentLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_details)

        storage = HabitStorage(this)
        habitId = intent.getLongExtra("habit_id", -1)

        val habits = storage.loadHabits()
        habit = habits.find { it.id == habitId }

        if (habit == null) {
            showCustomToast("Habit not found!", "error")
            finish()
            return
        }

        // 🔹 Initialize views
        title = findViewById(R.id.detailTitle)
        goalInput = findViewById(R.id.detailGoalInput)
        currentInput = findViewById(R.id.detailCurrentInput)
        updateBtn = findViewById(R.id.updateHabitBtn)
        deleteBtn = findViewById(R.id.deleteHabitBtn)
        backBtn = findViewById(R.id.backBtn)
        topImage = findViewById(R.id.detailImage)
        goalLabel = findViewById(R.id.detailGoalLabel)
        currentLabel = findViewById(R.id.detailCurrentLabel)

        title.text = habit!!.type.displayName
        goalInput.setText(habit!!.goal.toString())
        currentInput.setText(habit!!.currentValue.toString())

        // Change label text, image based on habit type
        when (habit!!.type) {
            HabitType.READING -> {
                goalLabel.text = "Pages per Day"
                currentLabel.text = "Pages Read"
                topImage.setImageResource(R.drawable.ic_book)
            }
            HabitType.EXERCISE -> {
                goalLabel.text = "Workout Minutes"
                currentLabel.text = "Completed Minutes"
                topImage.setImageResource(R.drawable.ic_exercise)
            }
            HabitType.STEPS -> {
                goalLabel.text = "Step Goal"
                currentLabel.text = "Steps Taken"
                topImage.setImageResource(R.drawable.ic_walk)
            }
            HabitType.MEDITATION -> {
                goalLabel.text = "Meditation Time (mins)"
                currentLabel.text = "Time Completed"
                topImage.setImageResource(R.drawable.ic_habits)
            }
            HabitType.WATER -> {
                goalLabel.text = "Glasses of Water"
                currentLabel.text = "Drank"
                topImage.setImageResource(R.drawable.ic_water)
            }
            else -> {
                goalLabel.text = "Daily Goal"
                currentLabel.text = "Current Value"
                topImage.setImageResource(R.drawable.lady1)
            }
        }

        // Update button
        updateBtn.setOnClickListener {
            val newGoal = goalInput.text.toString().toIntOrNull()
            val newCurrent = currentInput.text.toString().toIntOrNull()

            if (newGoal == null || newCurrent == null) {
                showCustomToast("Enter valid numbers", "error")
                return@setOnClickListener
            }

            habit!!.goal = newGoal
            habit!!.currentValue = newCurrent
            storage.saveHabits(habits)

            showCustomToast("Habit updated!", "success")
            setResult(Activity.RESULT_OK)
            finish()
        }

        // Delete button
        deleteBtn.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Habit")
                .setMessage("Are you sure you want to delete this habit?")
                .setPositiveButton("Yes") { _, _ ->
                    habits.remove(habit)
                    storage.saveHabits(habits)
                    showCustomToast("Habit deleted!", "success")
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }

        // Back button
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun showCustomToast(message: String, type: String) {

        val layout = layoutInflater.inflate(R.layout.custom_toast, null)

        val toastText = layout.findViewById<TextView>(R.id.toastText)
        val toastIcon = layout.findViewById<ImageView>(R.id.toastIcon)

        toastText.text = message

        val iconRes = if (type == "success") R.drawable.ic_success else R.drawable.ic_error
        toastIcon.setImageResource(iconRes)

        with(Toast(applicationContext)) {
            duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

}
