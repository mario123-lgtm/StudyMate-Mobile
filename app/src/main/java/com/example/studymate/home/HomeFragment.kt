package com.example.studymate.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.studymate.DaftarTugasActivity
import com.example.studymate.R

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(
            R.layout.activity_dashboard,
            container,
            false
        )
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar =
            view.findViewById<Toolbar>(R.id.toolbar)

        (requireActivity() as AppCompatActivity)
            .setSupportActionBar(toolbar)

        (requireActivity() as AppCompatActivity)
            .supportActionBar?.apply {
                title = "Home Dashboard"
                setDisplayHomeAsUpEnabled(false)
            }

        val btnDaftarTugas =
            view.findViewById<Button>(R.id.btnDaftarTugas)

        btnDaftarTugas.setOnClickListener {
            startActivity(
                Intent(
                    requireContext(),
                    DaftarTugasActivity::class.java
                )
            )
        }
    }
}