package com.example.studymate

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

data class TaskItem(
    val judul: String,
    val mataKuliah: String,
    val deadline: String,
    val prioritas: String
)

object TaskStorage {

    private const val PREF_NAME = "studymate_tasks"
    private const val KEY_TASKS = "tasks"

    fun addTask(
        context: Context,
        judul: String,
        mataKuliah: String,
        deadline: String,
        prioritas: String
    ) {
        val tasks = getTasks(context)

        tasks.add(
            TaskItem(
                judul,
                mataKuliah,
                deadline,
                prioritas
            )
        )

        saveTasks(context, tasks)
    }

    fun getTasks(context: Context): MutableList<TaskItem> {

        val pref =
            context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
            )

        val data =
            pref.getString(KEY_TASKS, "[]") ?: "[]"

        val array = JSONArray(data)

        val tasks =
            mutableListOf<TaskItem>()

        for (i in 0 until array.length()) {

            val obj = array.getJSONObject(i)

            tasks.add(
                TaskItem(
                    obj.getString("judul"),
                    obj.getString("mataKuliah"),
                    obj.getString("deadline"),
                    obj.getString("prioritas")
                )
            )
        }

        return tasks
    }

    fun saveTasks(
        context: Context,
        tasks: List<TaskItem>
    ) {

        val array = JSONArray()

        tasks.forEach { task ->

            val obj = JSONObject()

            obj.put("judul", task.judul)
            obj.put("mataKuliah", task.mataKuliah)
            obj.put("deadline", task.deadline)
            obj.put("prioritas", task.prioritas)

            array.put(obj)
        }

        context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )
            .edit()
            .putString(
                KEY_TASKS,
                array.toString()
            )
            .apply()
    }
}