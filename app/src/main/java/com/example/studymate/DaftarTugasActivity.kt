package com.example.studymate

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class DaftarTugasActivity : AppCompatActivity() {

    private lateinit var recyclerTugas: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_daftar_tugas)

        val toolbar =
            findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            title = "Daftar Tugas"
            setDisplayHomeAsUpEnabled(true)
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        recyclerTugas =
            findViewById(R.id.recyclerTugas)

        tampilkanTugas()

        val swipeCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {

                    val position =
                        viewHolder.bindingAdapterPosition

                    val tugasDihapus =
                        taskAdapter.tasks[position]

                    taskAdapter.tasks.removeAt(position)

                    taskAdapter.notifyItemRemoved(position)

                    TaskStorage.saveTasks(
                        this@DaftarTugasActivity,
                        taskAdapter.tasks
                    )

                    Snackbar.make(
                        recyclerTugas,
                        "Tugas berhasil dihapus",
                        Snackbar.LENGTH_LONG
                    )
                        .setAction("BATALKAN") {

                            taskAdapter.tasks.add(
                                position,
                                tugasDihapus
                            )

                            taskAdapter.notifyItemInserted(
                                position
                            )

                            TaskStorage.saveTasks(
                                this@DaftarTugasActivity,
                                taskAdapter.tasks
                            )
                        }
                        .show()
                }
            }

        ItemTouchHelper(swipeCallback)
            .attachToRecyclerView(recyclerTugas)
    }

    override fun onResume() {
        super.onResume()

        if (::recyclerTugas.isInitialized) {
            tampilkanTugas()
        }
    }

    private fun tampilkanTugas() {

        val tasks =
            TaskStorage.getTasks(this)

        taskAdapter =
            TaskAdapter(tasks)

        recyclerTugas.layoutManager =
            LinearLayoutManager(this)

        recyclerTugas.adapter =
            taskAdapter
    }
}