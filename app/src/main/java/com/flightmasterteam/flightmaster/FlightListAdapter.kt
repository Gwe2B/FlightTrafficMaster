package com.flightmasterteam.flightmaster

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FlightListAdapter(
        private val dataSet: List<FlightModel>,
        private val cellClickListener: OnCellClickListener
    ): RecyclerView.Adapter<FlightListAdapter.ViewHolder>() {

    interface OnCellClickListener {
        fun onCellClicked(flightModel: FlightModel)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cell = FlightCell(parent.context)
        cell.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return ViewHolder(cell)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flight = dataSet[position]
        val cell = holder.itemView as FlightCell

        cell.bindData(flight)
        cell.setOnClickListener() {
            cellClickListener.onCellClicked(flight)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}