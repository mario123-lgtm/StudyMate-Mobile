package com.example.studymate

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.data.api.SupabaseApiClient
import com.example.studymate.data.model.TugasModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class DaftarTugasActivity : AppCompatActivity() {

    private lateinit var recyclerTugas: RecyclerView

    private lateinit var taskAdapter:
            SupabaseTaskAdapter

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_daftar_tugas
        )

        val toolbar =
            findViewById<Toolbar>(
                R.id.toolbar
            )

        setSupportActionBar(toolbar)

        supportActionBar?.apply {

            title = "Daftar Tugas"

            setDisplayHomeAsUpEnabled(
                true
            )
        }

        toolbar.setNavigationOnClickListener {
            finish()
        }

        recyclerTugas =
            findViewById(
                R.id.recyclerTugas
            )

        recyclerTugas.layoutManager =
            LinearLayoutManager(this)

        taskAdapter =
            SupabaseTaskAdapter(
                mutableListOf()
            ) { task ->

                bukaEditTugas(task)
            }

        recyclerTugas.adapter =
            taskAdapter

        tampilkanTugas()

        val swipeCallback =
            object :
                ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or
                            ItemTouchHelper.RIGHT
                ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder:
                    RecyclerView.ViewHolder,
                    target:
                    RecyclerView.ViewHolder
                ): Boolean {

                    return false
                }

                override fun onSwiped(
                    viewHolder:
                    RecyclerView.ViewHolder,
                    direction: Int
                ) {

                    val position =
                        viewHolder
                            .bindingAdapterPosition

                    val tugas =
                        taskAdapter.tasks[position]

                    hapusTugas(
                        tugas,
                        position
                    )
                }
            }

        ItemTouchHelper(
            swipeCallback
        ).attachToRecyclerView(
            recyclerTugas
        )
    }

    private fun tampilkanTugas() {

        lifecycleScope.launch {

            try {

                val response =
                    SupabaseApiClient
                        .apiService
                        .getTugas(
                            SupabaseApiClient.API_KEY
                        )

                if (response.isSuccessful) {

                    val data =
                        response.body()
                            ?: emptyList()

                    taskAdapter.tasks.clear()

                    taskAdapter.tasks.addAll(
                        data
                    )

                    taskAdapter
                        .notifyDataSetChanged()

                } else {

                    val error =
                        response.errorBody()
                            ?.string()
                            ?: "Tidak ada detail error"

                    Toast.makeText(
                        this@DaftarTugasActivity,
                        "Gagal memuat tugas: $error",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@DaftarTugasActivity,
                    "Terjadi kesalahan: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun bukaEditTugas(
        task: TugasModel
    ) {

        val intent =
            Intent(
                this,
                TambahTugasActivity::class.java
            )

        intent.putExtra(
            "id",
            task.id
        )

        intent.putExtra(
            "judul",
            task.judul
        )

        intent.putExtra(
            "mataKuliah",
            task.kategori
        )

        intent.putExtra(
            "deadline",
            task.deadline
        )

        intent.putExtra(
            "prioritas",
            task.prioritas
        )

        startActivity(intent)
    }

    private fun hapusTugas(
        tugas: TugasModel,
        position: Int
    ) {

        lifecycleScope.launch {

            try {

                val response =
                    SupabaseApiClient
                        .apiService
                        .hapusTugas(
                            SupabaseApiClient.API_KEY,
                            "eq.${tugas.id}"
                        )

                if (response.isSuccessful) {

                    taskAdapter.tasks
                        .removeAt(position)

                    taskAdapter
                        .notifyItemRemoved(
                            position
                        )

                    Snackbar.make(
                        recyclerTugas,
                        "Tugas berhasil dihapus",
                        Snackbar.LENGTH_LONG
                    ).show()

                } else {

                    taskAdapter
                        .notifyItemChanged(
                            position
                        )

                    val error =
                        response.errorBody()
                            ?.string()
                            ?: "Tidak ada detail error"

                    Toast.makeText(
                        this@DaftarTugasActivity,
                        "Hapus gagal: $error",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                taskAdapter
                    .notifyItemChanged(
                        position
                    )

                Toast.makeText(
                    this@DaftarTugasActivity,
                    "Terjadi kesalahan: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onResume() {

        super.onResume()

        if (::taskAdapter.isInitialized) {
            tampilkanTugas()
        }
    }
}