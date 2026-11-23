package com.example.studymate.data.api

import com.example.studymate.data.model.ReminderDeadlineModel
import com.example.studymate.data.model.ReminderDeadlineRequest
import com.example.studymate.data.model.TugasModel
import com.example.studymate.data.model.TugasRequest
import com.example.studymate.data.model.UserModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface SupabaseApiService {

    // =========================
    // REGISTER USER
    // =========================

    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @POST("rest/v1/users")
    suspend fun registerUser(
        @Header("apikey") apiKey: String,
        @Body user: UserModel
    ): Response<Unit>


    // =========================
    // CREATE TUGAS
    // =========================

    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @POST("rest/v1/tugas")
    suspend fun tambahTugas(
        @Header("apikey") apiKey: String,
        @Body tugas: TugasRequest
    ): Response<Unit>


    // =========================
    // READ TUGAS
    // =========================

    @GET("rest/v1/tugas")
    suspend fun getTugas(
        @Header("apikey") apiKey: String,
        @Query("select") select: String = "*",
        @Query("order") order: String = "created_at.desc"
    ): Response<List<TugasModel>>


    // =========================
    // UPDATE TUGAS
    // =========================

    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @PATCH("rest/v1/tugas")
    suspend fun updateTugas(
        @Header("apikey") apiKey: String,
        @Query("id") id: String,
        @Body tugas: TugasRequest
    ): Response<Unit>


    // =========================
    // DELETE TUGAS
    // =========================

    @DELETE("rest/v1/tugas")
    suspend fun hapusTugas(
        @Header("apikey") apiKey: String,
        @Query("id") id: String
    ): Response<Unit>


    // =========================
    // CREATE REMINDER
    // =========================

    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @POST("rest/v1/reminder_deadline")
    suspend fun tambahReminder(
        @Header("apikey") apiKey: String,
        @Body reminder: ReminderDeadlineRequest
    ): Response<Unit>


    // =========================
    // READ REMINDER
    // =========================

    @GET("rest/v1/reminder_deadline")
    suspend fun getReminder(
        @Header("apikey") apiKey: String,
        @Query("select") select: String = "*",
        @Query("order") order: String = "created_at.desc"
    ): Response<List<ReminderDeadlineModel>>


    // =========================
    // UPDATE REMINDER
    // =========================

    @Headers(
        "Content-Type: application/json",
        "Prefer: return=minimal"
    )
    @PATCH("rest/v1/reminder_deadline")
    suspend fun updateReminder(
        @Header("apikey") apiKey: String,
        @Query("id") id: String,
        @Body reminder: ReminderDeadlineRequest
    ): Response<Unit>


    // =========================
    // DELETE REMINDER
    // =========================

    @DELETE("rest/v1/reminder_deadline")
    suspend fun hapusReminder(
        @Header("apikey") apiKey: String,
        @Query("id") id: String
    ): Response<Unit>
}