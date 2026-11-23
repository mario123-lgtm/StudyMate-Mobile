package com.example.studymate.data.model

data class ReminderDeadlineModel(
    val id: Long,
    val judul: String,
    val tanggal: String,
    val jam: String,
    val keterangan: String,
    val status: String
)

data class ReminderDeadlineRequest(
    val judul: String,
    val tanggal: String,
    val jam: String,
    val keterangan: String,
    val status: String
)