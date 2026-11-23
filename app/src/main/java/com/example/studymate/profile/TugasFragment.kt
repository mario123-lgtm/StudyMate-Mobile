package com.example.studymate.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.R
import com.example.studymate.SupabaseTaskAdapter
import com.example.studymate.TambahTugasActivity
import com.example.studymate.data.api.SupabaseApiClient
import com.example.studymate.data.model.TugasModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class TugasFragment : Fragment() {

    private lateinit var recyclerTugas: RecyclerView
    private lateinit var taskAdapter: SupabaseTaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(
            R.layout.activity_daftar_tugas,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        recyclerTugas =
            view.findViewById(
                R.id.recyclerTugas
            )

        recyclerTugas.layoutManager =
            LinearLayoutManager(
                requireContext()
            )

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

                    if (
                        position !=
                        RecyclerView.NO_POSITION
                    ) {

                        val tugas =
                            taskAdapter.tasks[position]

                        hapusTugas(
                            tugas,
                            position
                        )
                    }
                }
            }

        ItemTouchHelper(
            swipeCallback
        ).attachToRecyclerView(
            recyclerTugas
        )
    }

    // =========================
    // READ DATA SUPABASE
    // =========================

    private fun tampilkanTugas() {

        viewLifecycleOwner.lifecycleScope.launch {

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
                        requireContext(),
                        "Gagal memuat tugas: $error",
                        Toast.LENGTH_LONG
                    ).show()
                }

            } catch (e: Exception) {

                Toast.makeText(
                    requireContext(),
                    "Terjadi kesalahan: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // =========================
    // UPDATE
    // Klik item untuk edit
    // =========================

    private fun bukaEditTugas(
        task: TugasModel
    ) {

        val intent =
            Intent(
                requireContext(),
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

    // =========================
    // DELETE
    // Swipe kiri / kanan
    // =========================

    private fun hapusTugas(
        tugas: TugasModel,
        position: Int
    ) {

        viewLifecycleOwner.lifecycleScope.launch {

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

                    val snackbar =
                        Snackbar.make(
                            recyclerTugas,
                            "Tugas berhasil dihapus",
                            Snackbar.LENGTH_LONG
                        )

                    val bottomNav =
                        requireActivity()
                            .findViewById<View>(
                                R.id.bottom_nav_view
                            )

                    snackbar.anchorView =
                        bottomNav

                    snackbar.show()

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
                        requireContext(),
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
                    requireContext(),
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