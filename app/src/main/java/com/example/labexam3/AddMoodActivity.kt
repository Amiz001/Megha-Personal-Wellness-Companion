package com.example.labexam3

import android.content.Context
import android.content.res.ColorStateList
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.labexam3.model.Mood
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class AddMoodActivity : AppCompatActivity() {

    private lateinit var moodUnhappy: LinearLayout
    private lateinit var moodSad: LinearLayout
    private lateinit var moodNormal: LinearLayout
    private lateinit var moodGood: LinearLayout
    private lateinit var moodHappy: LinearLayout
    private lateinit var btnSubmitMood: MaterialButton

    private var selectedMood: Mood? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_mood)

        moodUnhappy = findViewById(R.id.moodUnhappy)
        moodSad = findViewById(R.id.moodSad)
        moodNormal = findViewById(R.id.moodNormal)
        moodGood = findViewById(R.id.moodGood)
        moodHappy = findViewById(R.id.moodHappy)
        btnSubmitMood = findViewById(R.id.btnSubmitMood)


        moodUnhappy.setOnClickListener { selectMood(R.drawable.ic_unhappy, "Unhappy") }
        moodSad.setOnClickListener { selectMood(R.drawable.ic_bad, "Sad") }
        moodNormal.setOnClickListener { selectMood(R.drawable.ic_normal, "Normal") }
        moodGood.setOnClickListener { selectMood(R.drawable.ic_good, "Good") }
        moodHappy.setOnClickListener { selectMood(R.drawable.ic_happy, "Happy") }

        btnSubmitMood.setOnClickListener {
            if (selectedMood == null) {
                showCustomToast("Please select a mood", "error")
                return@setOnClickListener
            }

            saveMood(selectedMood!!)
            showCustomToast("Mood noted!", "success")
            finish()
        }

        val backText = findViewById<TextView>(R.id.backText)
        backText.setOnClickListener {
            finish()
        }
    }

    private fun selectMood(iconRes: Int, moodName: String) {
        val currentDate = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(Date())
        selectedMood = Mood(iconRes, moodName, currentDate)
        showCustomToast("Selected: $moodName", "success")
    }

    private fun saveMood(mood: Mood) {
        val sharedPref = getSharedPreferences("mood_data", Context.MODE_PRIVATE)
        val gson = Gson()
        val type = object : TypeToken<MutableList<Mood>>() {}.type

        val existingJson = sharedPref.getString("mood_list", null)
        val moodList: MutableList<Mood> = if (existingJson != null) {
            gson.fromJson(existingJson, type)
        } else mutableListOf()

        moodList.add(0, mood) // latest first

        val newJson = gson.toJson(moodList)
        sharedPref.edit().putString("mood_list", newJson).apply()
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
