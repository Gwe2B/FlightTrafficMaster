package com.flightmasterteam.flightmaster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FlightListActivity : AppCompatActivity(), FlightListAdapter.OnCellClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flight_list)

        val begin = intent.getLongExtra("BEGIN", 0)
        val end = intent.getLongExtra("END", 0)
        val isArrival = intent.getBooleanExtra("IS_ARRIVAL", false)
        val icao = intent.getStringExtra("ICAO")

        val viewModel = ViewModelProvider(this).get(FlightListViewModel::class.java)
        viewModel.doRequest(begin, end, isArrival, icao!!)

        viewModel.getFlightListLiveData().observe(this, Observer {
            // Retrieve the recycler view
            var recyclerView = findViewById<RecyclerView>(R.id.flights_recycler_view)

            // Attach an Adapter
            recyclerView.adapter = FlightListAdapter(it, this)

            // Attach a LayoutManager
            recyclerView.layoutManager = LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false
            )
        })
    }

    override fun onCellClicked(flightModel: FlightModel) {
        TODO("Not yet implemented")
    }
}