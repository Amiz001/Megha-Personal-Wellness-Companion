package com.example.labexam3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.labexam3.model.Mood

class MoodAdapter(private val moodList: List<Mood>) :
    RecyclerView.Adapter<MoodAdapter.MoodViewHolder>() {

    inner class MoodViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val icon: ImageView = itemView.findViewById(R.id.ivMoodIcon)
        val moodName: TextView = itemView.findViewById(R.id.tvMoodName)
        val date: TextView = itemView.findViewById(R.id.tvMoodDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_mood, parent, false)
        return MoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoodViewHolder, position: Int) {
        val mood = moodList[position]
        holder.icon.setImageResource(mood.iconRes)
        holder.moodName.text = mood.moodName
        holder.date.text = mood.date
    }

    override fun getItemCount(): Int = moodList.size
}
