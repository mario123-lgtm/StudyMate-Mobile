package com.example.studymate.data.model

data class TugasModel(
    val id: Long,
    val judul: String,
    val deadline: String,
    val prioritas: String,
    val kategori: String,
    val catatan: String? = null,
    val status: String? = null
)

data class TugasRequest(
    val judul: String,
    val deadline: String,
    val prioritas: String,
    val kategori: String
)