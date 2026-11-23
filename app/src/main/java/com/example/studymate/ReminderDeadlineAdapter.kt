package com.example.studymate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.data.model.ReminderDeadlineModel

class ReminderDeadlineAdapter(
    val reminders: MutableList<ReminderDeadlineModel>,
    private val onItemClick: (ReminderDeadlineModel) -> Unit
) : RecyclerView.Adapter<ReminderDeadlineAdapter.ReminderViewHolder>() {

    class ReminderViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        val tvJudul: TextView =
            itemView.findViewById(
                R.id.tvJudulReminder
            )

        val tvTanggal: TextView =
            itemView.findViewById(
                R.id.tvTanggalReminder
            )

        val tvKeterangan: TextView =
            itemView.findViewById(
                R.id.tvKeteranganReminder
            )

        val tvStatus: TextView =
            itemView.findViewById(
                R.id.tvStatusReminder
            )
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReminderViewHolder {

        val view =
            LayoutInflater.from(parent.context)
                .inflate(
                    R.layout.item_reminder_deadline,
                    parent,
                    false
                )

        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ReminderViewHolder,
        position: Int
    ) {

        val reminder =
            reminders[position]

        holder.tvJudul.text =
            reminder.judul

        holder.tvTanggal.text =
            "${reminder.tanggal} • ${reminder.jam}"

        holder.tvKeterangan.text =
            reminder.keterangan

        holder.tvStatus.text =
            reminder.status

        holder.itemView.setOnClickListener {

            onItemClick(reminder)
        }
    }

    override fun getItemCount(): Int {
        return reminders.size
    }
}