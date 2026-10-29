package com.example.studymate

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    val tasks: MutableList<TaskItem>
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val judul: TextView =
            itemView.findViewById(R.id.tvJudulTugas)

        val mataKuliah: TextView =
            itemView.findViewById(R.id.tvMataKuliah)

        val deadline: TextView =
            itemView.findViewById(R.id.tvDeadline)

        val prioritas: TextView =
            itemView.findViewById(R.id.tvPrioritas)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TaskViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_tugas,
                    parent,
                    false
                )

        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TaskViewHolder,
        position: Int
    ) {

        val task = tasks[position]

        holder.judul.text =
            task.judul

        holder.mataKuliah.text =
            task.mataKuliah

        holder.deadline.text =
            "Deadline: ${task.deadline}"

        holder.prioritas.text =
            task.prioritas

        val backgroundPrioritas =
            GradientDrawable()

        backgroundPrioritas.cornerRadius =
            18f

        when (task.prioritas) {

            "Tinggi" -> {

                backgroundPrioritas.setColor(
                    Color.parseColor("#FFE1E1")
                )

                holder.prioritas.setTextColor(
                    Color.parseColor("#E53935")
                )
            }

            "Sedang" -> {

                backgroundPrioritas.setColor(
                    Color.parseColor("#FFF1C2")
                )

                holder.prioritas.setTextColor(
                    Color.parseColor("#C78300")
                )
            }

            "Rendah" -> {

                backgroundPrioritas.setColor(
                    Color.parseColor("#DDF7E5")
                )

                holder.prioritas.setTextColor(
                    Color.parseColor("#159447")
                )
            }

            else -> {

                backgroundPrioritas.setColor(
                    Color.parseColor("#E8E8E8")
                )

                holder.prioritas.setTextColor(
                    Color.DKGRAY
                )
            }
        }

        holder.prioritas.background =
            backgroundPrioritas

        holder.prioritas.setPadding(
            18,
            8,
            18,
            8
        )
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}