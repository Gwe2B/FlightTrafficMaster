package com.flightmasterteam.flightmaster

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class FlightCell : LinearLayout {

    lateinit var depDateTextView: TextView
    lateinit var depAirportTextView: TextView
    lateinit var depHourTextView: TextView
    lateinit var arrDateTextView: TextView
    lateinit var arrAirportTextView: TextView
    lateinit var arrHourTextView: TextView
    lateinit var flightDurationTextView: TextView
    lateinit var flightNameTextView: TextView


    constructor(context: Context?) : super(context) {
        initLayout()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initLayout()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initLayout()
    }

    private fun bindViews() {
        // make the find view by ids for your view
        depDateTextView = findViewById(R.id.depDateTextView)
        depAirportTextView= findViewById(R.id.depAirportTextView)
        depHourTextView= findViewById(R.id.depTimeTextView)
        arrDateTextView= findViewById(R.id.arrDateTextView)
        arrAirportTextView= findViewById(R.id.arrAirportTextView)
        arrHourTextView= findViewById(R.id.arrTimeTextView)
        flightDurationTextView= findViewById(R.id.flightDurationTextView)
        flightNameTextView= findViewById(R.id.flightNumberTextView)
    }

    fun bindData(flight: FlightModel) {
        Log.d("TAG", "message")
        val depTime = Date()
        val arrTime = Date()

        depTime.time = flight.firstSeen * 1000
        arrTime.time = flight.lastSeen * 1000

        //fill your views
        depDateTextView.text = Utils.dateToString(depTime)
        depAirportTextView.text = flight.estDepartureAirport
        depHourTextView.text = android.text.format.DateFormat.format("HH:mm", depTime)
        flightNameTextView.text = flight.callsign
        flightDurationTextView.text = (flight.lastSeen - flight.firstSeen).toString()
        arrDateTextView.text = Utils.dateToString(arrTime)
        arrAirportTextView.text = flight.estArrivalAirport
        arrHourTextView.text = android.text.format.DateFormat.format("HH:mm", arrTime)
    }

    private fun initLayout() {
        LayoutInflater.from(context).inflate(R.layout.flight_cell, this, true)
        bindViews()
    }

}