package com.flightmasterteam.flightmaster

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * A simple [Fragment] subclass.
 * Use the [FlightListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FlightListFragment : Fragment(), FlightListAdapter.OnCellClickListener {
    private lateinit var viewModel: FlightListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flight_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(FlightListViewModel::class.java)
        viewModel.doFlightListRequest()

        viewModel.getFlightListLiveData().observe(viewLifecycleOwner, Observer {
            // Retrieve the recycler view
            var recyclerView = view.findViewById<RecyclerView>(R.id.flights_recycler_view)

            // Attach an Adapter
            recyclerView.adapter = FlightListAdapter(it, this)

            // Attach a LayoutManager
            recyclerView.layoutManager = LinearLayoutManager(
                requireActivity(),
                LinearLayoutManager.VERTICAL,
                false
            )
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment FlightListFragment.
         */
        @JvmStatic
        fun newInstance() =
            FlightListFragment().apply {
                arguments = Bundle().apply {}
            }
    }

    override fun onCellClicked(flightModel: FlightModel) {
        viewModel.setClickedFlightLiveData(flightModel)
    }
}