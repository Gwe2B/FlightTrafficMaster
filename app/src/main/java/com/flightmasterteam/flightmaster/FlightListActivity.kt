package com.flightmasterteam.flightmaster

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider

class FlightListActivity : AppCompatActivity() {
    var instance: FlightListActivity? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this;
        setContentView(R.layout.activity_flight_list)

        val isTablet = findViewById<FragmentContainerView>(R.id.fragment_flight_map) != null

        val viewModel = ViewModelProvider(this).get(FlightListViewModel::class.java)
        viewModel.depTime = intent.getLongExtra("BEGIN", 0)
        viewModel.arrTime = intent.getLongExtra("END", 0)
        viewModel.isArrival = intent.getBooleanExtra("IS_ARRIVAL", false)
        viewModel.icao = intent.getStringExtra("ICAO")!!

        viewModel.getClickedFlightLiveData().observe(this) {
            // Display the right flight
            if (!isTablet) {
                // Replace the fragment
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_flight_list, MapFragment())
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        viewModel.getErrorLiveData().observe(this) {
            if (it != null) {
                displayErrorMessage(it)
                viewModel.setErrorLiveData(null)
            }
        }
    }

    private fun displayErrorMessage(msg: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(msg)
        builder.show()
    }
}