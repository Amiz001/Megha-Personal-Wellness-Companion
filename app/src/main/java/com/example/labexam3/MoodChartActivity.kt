package com.example.labexam3

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.labexam3.model.Mood
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class MoodChartActivity : AppCompatActivity() {

    private lateinit var moodChart: LineChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mood_chart)

        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.topAppBar)
        toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }

        moodChart = findViewById(R.id.moodChart)
        setupChart()
    }

    private fun setupChart() {
        val sharedPref = getSharedPreferences("mood_data", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPref.getString("mood_list", null)
        val type = object : TypeToken<MutableList<Mood>>() {}.type
        val moodList: MutableList<Mood> =
            if (json != null) gson.fromJson(json, type) else mutableListOf()

        if (moodList.isEmpty()) return

        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        moodList.sortBy {
            try { dateFormat.parse(it.date) } catch (e: Exception) { Date() }
        }

        val moodValues = mapOf(
            R.drawable.ic_unhappy to 1f,
            R.drawable.ic_bad to 2f,
            R.drawable.ic_normal to 3f,
            R.drawable.ic_good to 4f,
            R.drawable.ic_happy to 5f
        )

        val entries = mutableListOf<Entry>()
        val xLabels = mutableListOf<String>()

        moodList.forEachIndexed { index, mood ->
            val yValue = moodValues[mood.iconRes] ?: 3f
            val entry = Entry(index.toFloat(), yValue)
            entry.icon = ContextCompat.getDrawable(this, mood.iconRes)
            entries.add(entry)

            val dateLabel = mood.date.split(",")[0] + mood.date.split(",")[1]
            xLabels.add(dateLabel)
        }

        val dataSet = LineDataSet(entries, "Mood Level").apply {
            color = ContextCompat.getColor(this@MoodChartActivity, R.color.colorPrimary)
            setCircleColor(ContextCompat.getColor(this@MoodChartActivity, R.color.colorPrimary))
            lineWidth = 2.5f
            circleRadius = 5f
            setDrawCircles(false)
            setDrawIcons(true)
            setDrawCircleHole(false)
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
            setDrawFilled(true)
            fillColor = ContextCompat.getColor(this@MoodChartActivity, R.color.colorPrimary)
            fillAlpha = 80
        }

        moodChart.apply {
            data = LineData(dataSet)
            setBackgroundColor(Color.WHITE)
            legend.isEnabled = false
            description.isEnabled = false
            animateX(1000)

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = 5.5f
                granularity = 1f
                textColor = ContextCompat.getColor(this@MoodChartActivity, R.color.textPrimary)
                valueFormatter = IndexAxisValueFormatter(listOf("", "Unhappy", "Sad", "Normal", "Good", "Happy"))
            }

            axisRight.isEnabled = false

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                textColor = ContextCompat.getColor(this@MoodChartActivity, R.color.textPrimary)
                setDrawGridLines(false)
                granularity = 1f
                labelRotationAngle = -25f
                valueFormatter = IndexAxisValueFormatter(xLabels)
                textSize = 10f
            }

            invalidate()
        }
    }
}
