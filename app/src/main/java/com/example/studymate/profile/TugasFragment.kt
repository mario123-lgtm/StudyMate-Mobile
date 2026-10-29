package com.example.studymate.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studymate.R
import com.example.studymate.TaskAdapter
import com.example.studymate.TaskStorage
import com.google.android.material.snackbar.Snackbar

class TugasFragment : Fragment() {

    private lateinit var recyclerTugas: RecyclerView
    private lateinit var taskAdapter: TaskAdapter

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
            view.findViewById(R.id.recyclerTugas)

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
                        requireContext(),
                        taskAdapter.tasks
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

                    snackbar.anchorView = bottomNav

                    snackbar.setAction("BATALKAN") {

                        taskAdapter.tasks.add(
                            position,
                            tugasDihapus
                        )

                        taskAdapter.notifyItemInserted(
                            position
                        )

                        TaskStorage.saveTasks(
                            requireContext(),
                            taskAdapter.tasks
                        )
                    }

                    snackbar.show()
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
            TaskStorage.getTasks(requireContext())

        taskAdapter =
            TaskAdapter(tasks)

        recyclerTugas.layoutManager =
            LinearLayoutManager(requireContext())

        recyclerTugas.adapter =
            taskAdapter
    }
}