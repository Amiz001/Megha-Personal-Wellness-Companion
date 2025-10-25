package com.example.labexam3

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labexam3.data.HabitStorage
import com.example.labexam3.model.Habit
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*
import androidx.activity.result.contract.ActivityResultContracts

class HabitsFragment : Fragment() {

    private lateinit var recycler: RecyclerView
    private lateinit var storage: HabitStorage
    private lateinit var habits: MutableList<Habit>
    private lateinit var adapter: HabitAdapter

    private val addHabitLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadHabits()
        }
    }

    private val updateHabitLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadHabits()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_habits_fragment, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Top dates
        val dateContainer = view.findViewById<LinearLayout>(R.id.dateContainer)
        val today = LocalDate.now()
        val startOfWeek = today.with(java.time.DayOfWeek.MONDAY)

        for (i in 0..6) {
            val date = startOfWeek.plusDays(i.toLong())
            val isToday = date == today
            val isPast = date.isBefore(today)

            val dayLayout = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(dpToPx(60), dpToPx(80)).apply {
                    setMargins(dpToPx(4), 0, dpToPx(4), 0)
                }
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                background = when {
                    isToday -> ContextCompat.getDrawable(requireContext(), R.drawable.date_bg_black)
                    isPast -> ContextCompat.getDrawable(requireContext(), R.drawable.date_bg_green)
                    else -> ContextCompat.getDrawable(requireContext(), R.drawable.date_bg_white)
                }
            }

            val dayNum = TextView(requireContext()).apply {
                text = date.dayOfMonth.toString()
                textSize = 20f
                setTextColor(if (isToday) android.graphics.Color.WHITE else android.graphics.Color.BLACK)
                setTypeface(typeface, android.graphics.Typeface.BOLD)
                gravity = Gravity.CENTER
            }

            val dayName = TextView(requireContext()).apply {
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                textSize = 14f
                setTextColor(if (isToday) android.graphics.Color.WHITE else android.graphics.Color.BLACK)
                gravity = Gravity.CENTER
            }

            dayLayout.addView(dayNum)
            dayLayout.addView(dayName)
            dateContainer.addView(dayLayout)
        }

        // Habit list
        recycler = view.findViewById(R.id.recyclerHabits)
        recycler.layoutManager = GridLayoutManager(requireContext(), 2)

        storage = HabitStorage(requireContext())
        habits = storage.loadHabits()

        adapter = HabitAdapter(habits) { habit, isAddCard ->
            if (isAddCard) {
                val intent = Intent(requireContext(), AddHabitActivity::class.java)
                addHabitLauncher.launch(intent)
            } else {
                val intent = Intent(requireContext(), HabitDetailsActivity::class.java)
                intent.putExtra("habit_id", habit?.id)
                updateHabitLauncher.launch(intent)
            }
        }

        recycler.adapter = adapter

        return view
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun loadHabits() {
        habits.clear()
        habits.addAll(storage.loadHabits())
        adapter.notifyDataSetChanged()
    }
}
