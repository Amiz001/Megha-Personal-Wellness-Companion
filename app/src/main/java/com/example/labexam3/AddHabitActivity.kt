package com.example.labexam3

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.labexam3.R
import com.example.labexam3.data.HabitStorage
import com.example.labexam3.model.Habit
import com.example.labexam3.model.HabitType

class AddHabitActivity : AppCompatActivity() {

    private lateinit var storage: HabitStorage
    private lateinit var spinner: Spinner
    private lateinit var goalInput: EditText
    private lateinit var saveBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_habit)

        storage = HabitStorage(this)

        spinner = findViewById(R.id.habitTypeSpinner)
        goalInput = findViewById(R.id.goalInput)
        saveBtn = findViewById(R.id.saveHabitBtn)

        val types = HabitType.values().map { it.displayName }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        //Back button
        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

        // Change input hint based on selected habit
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedType = HabitType.values()[position]
                when (selectedType.name) {
                    "READING" -> goalInput.hint = "Enter minutes to read"
                    "STEPS" -> goalInput.hint = "Enter step count goal"
                    "WATER" -> goalInput.hint = "Enter number of litres"
                    "SLEEP" -> goalInput.hint = "Enter hours of sleep"
                    "MEDITATION" -> goalInput.hint = "Enter minutes to meditate"
                    "EXERCISE" -> goalInput.hint = "Enter workout duration"
                    else -> goalInput.hint = "Enter daily goal"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        saveBtn.setOnClickListener {
            val selectedType = HabitType.values()[spinner.selectedItemPosition]
            val goalText = goalInput.text.toString().trim()

            if (goalText.isEmpty()) {
                showCustomToast("Enter a goal", "error")
                return@setOnClickListener
            }

            val habits = storage.loadHabits()

            //Prevent duplicate
            val alreadyExists = habits.any { it.type == selectedType }
            if (alreadyExists) {
                showCustomToast("${selectedType.displayName} already added!", "error")
                return@setOnClickListener
            }

            val habit = Habit(type = selectedType, goal = goalText.toInt())
            habits.add(habit)
            storage.saveHabits(habits)

            showCustomToast("Habit Added!", "success")
            setResult(RESULT_OK, intent)
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
