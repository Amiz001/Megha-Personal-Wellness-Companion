package com.example.labexam3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labexam3.model.Mood
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MoodFragment : Fragment() {

    private lateinit var recyclerMoods: RecyclerView
    private lateinit var fabAddMood: FloatingActionButton
    private lateinit var btnViewChart: Button
    private lateinit var btnClearMoods: Button
    private lateinit var moodAdapter: MoodAdapter
    private var moodList: MutableList<Mood> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_mood_fragment, container, false)

        recyclerMoods = view.findViewById(R.id.recyclerMoods)
        fabAddMood = view.findViewById(R.id.fabAddMood)
        btnViewChart = view.findViewById(R.id.btnViewChart)
        btnClearMoods = view.findViewById(R.id.btnClearMoods)

        recyclerMoods.layoutManager = LinearLayoutManager(requireContext())
        loadMoods()

        fabAddMood.setOnClickListener {
            val intent = Intent(requireContext(), AddMoodActivity::class.java)
            startActivity(intent)
        }

        btnViewChart.setOnClickListener {
            val intent = Intent(requireContext(), MoodChartActivity::class.java)
            startActivity(intent)
        }


        return view
    }

    override fun onResume() {
        super.onResume()
        loadMoods()
    }

    private fun loadMoods() {
        val sharedPref = requireContext().getSharedPreferences("mood_data", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("mood_list", null)
        val type = object : TypeToken<MutableList<Mood>>() {}.type
        moodList = if (json != null) gson.fromJson(json, type) else mutableListOf()

        moodAdapter = MoodAdapter(moodList)
        recyclerMoods.adapter = moodAdapter
    }
}
