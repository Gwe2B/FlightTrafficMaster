package com.flightmasterteam.flightmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FlightListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_list)

        val begin = intent.getLongExtra("BEGIN", 0)
        val end = intent.getLongExtra("END", 0)
        val isArrival = intent.getBooleanExtra("IS_ARRIVAL", false)
        val icao = intent.getStringExtra("ICAO")

        val viewModel = ViewModelProvider(this).get(FlightListViewModel::class.java)
        viewModel.doRequest(begin, end, isArrival, icao!!)
    }
}