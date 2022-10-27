package com.flightmasterteam.flightmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FlightListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_list)

        val isTablet = findViewById<FragmentContainerView>(R.id.fragment_flight_map) != null

        val viewModel = ViewModelProvider(this).get(FlightListViewModel::class.java)
        viewModel.depTime = intent.getLongExtra("BEGIN", 0)
        viewModel.arrTime = intent.getLongExtra("END", 0)
        viewModel.isArrival = intent.getBooleanExtra("IS_ARRIVAL", false)
        viewModel.icao = intent.getStringExtra("ICAO")!!

        viewModel.getClickedFlightLiveData().observe(this, Observer {
            // Display the right flight
            if(!isTablet) {
                // Replace the fragment
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_flight_list, FlightMapFragment.newInstance())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        })
    }
}