package com.example.labexam3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SettingsFragment : Fragment() {

    private lateinit var tvHydrationInterval: TextView
    private lateinit var btnSetHydration: MaterialButton
    private lateinit var btnClearMoods: MaterialButton
    private lateinit var btnClearHabits: MaterialButton
    private lateinit var btnShareMoodSummary: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_settings_fragment, container, false)

        // Initialize views
        tvHydrationInterval = view.findViewById(R.id.tvHydrationInterval)
        btnSetHydration = view.findViewById(R.id.btnSetHydration)
        btnClearMoods = view.findViewById(R.id.btnClearMoods)
        btnClearHabits = view.findViewById(R.id.btnClearHabits)
        btnShareMoodSummary = view.findViewById(R.id.btnShareMoodSummary)

        loadInterval()


        btnSetHydration.setOnClickListener {
            val intent = Intent(requireContext(), HydrationReminderActivity::class.java)
            startActivity(intent)
        }


        btnClearMoods.setOnClickListener {
            showClearConfirmationDialog(
                title = "Clear All Moods?",
                message = "This will permanently delete all your mood entries.",
                onConfirm = { clearMoods() }
            )
        }


        btnClearHabits.setOnClickListener {
            showClearConfirmationDialog(
                title = "Clear All Habits?",
                message = "This will permanently delete all your habit entries.",
                onConfirm = { clearHabits() }
            )
        }

        btnShareMoodSummary.setOnClickListener {
            shareMoodSummary()
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        loadInterval() // refresh if user changed it
    }


    private fun loadInterval() {
        val pref = requireContext().getSharedPreferences("hydration", Context.MODE_PRIVATE)
        val interval = pref.getInt("interval_hours", 0)
        tvHydrationInterval.text = if (interval == 0)
            "Current Interval: Not Set"
        else
            "Current Interval: Every $interval hour(s)"
    }

    private fun showClearConfirmationDialog(title: String, message: String, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("Clear") { dialog, _ ->
                onConfirm()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun clearMoods() {
        val sharedPref = requireContext().getSharedPreferences("mood_data", Context.MODE_PRIVATE)
        sharedPref.edit().remove("mood_list").apply() // remove the actual mood list key
        showCustomToast("All moods cleared successfully", "success")
    }


    private fun clearHabits() {
        val sharedPref = requireContext().getSharedPreferences("habit_prefs", Context.MODE_PRIVATE)
        sharedPref.edit().remove("habit_list").apply()
        showCustomToast("All habits cleared successfully", "success")
    }


    // Share mood summary
    private fun shareMoodSummary() {
        val sharedPref = requireContext().getSharedPreferences("mood_data", Context.MODE_PRIVATE)
        val gson = com.google.gson.Gson()
        val json = sharedPref.getString("mood_list", null)

        // Convert JSON → List<Mood>
        val type = object : com.google.gson.reflect.TypeToken<MutableList<com.example.labexam3.model.Mood>>() {}.type
        val moodList: MutableList<com.example.labexam3.model.Mood> =
            if (json != null) gson.fromJson(json, type) else mutableListOf()

        // Build summary text
        val totalMoods = moodList.size
        val commonMood = moodList.groupBy { it.moodName }
            .maxByOrNull { it.value.size }
            ?.key ?: "N/A"
        val firstDate = moodList.minByOrNull { it.date }?.date ?: "N/A"

        val summaryText = buildString {
            append("📊 My Mood Summary\n\n")
            append("Here’s an overview of my mood tracking:\n")
            append("• Total moods logged: $totalMoods\n")
            append("• Most common mood: $commonMood\n")
            append("• Tracking since: $firstDate\n\n")
            append("Keep tracking your moods to build healthier habits!\n")
            append("\n💚 Shared via My Wellness Tracker App")
        }

        // Create share intent
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Mood Summary")
        shareIntent.putExtra(Intent.EXTRA_TEXT, summaryText)


        try {
            startActivity(Intent.createChooser(shareIntent, "Share Mood Summary via"))
        } catch (e: Exception) {
            showCustomToast("Unable to share", "error")
        }
    }

    private fun showCustomToast(message: String, type: String) {

        val layout = layoutInflater.inflate(R.layout.custom_toast, null)

        val toastText = layout.findViewById<TextView>(R.id.toastText)
        val toastIcon = layout.findViewById<ImageView>(R.id.toastIcon)

        toastText.text = message

        val iconRes = if (type == "success") R.drawable.ic_success else R.drawable.ic_error
        toastIcon.setImageResource(iconRes)

        with(Toast(requireContext())) {
        duration = Toast.LENGTH_SHORT
            view = layout
            show()
        }
    }

}
