package com.flightmasterteam.flightmaster

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

/**
 * A simple [Fragment] subclass.
 * Use the [FlightMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FlightMapFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity()).get(FlightListViewModel::class.java)
        viewModel.getClickedFlightLiveData().observe(viewLifecycleOwner, Observer {
            Log.i("MAP", it.toString())
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment FlightMapFragment.
         */
        @JvmStatic
        fun newInstance() =
            FlightMapFragment().apply {
                arguments = Bundle().apply {}
            }
    }
}