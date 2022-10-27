package com.flightmasterteam.flightmaster

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class FlightListViewModel: ViewModel() {
    private var DEPARTURE_API_URL = "https://opensky-network.org/api/flights/departure"
    private var ARRIVAL_API_URL = "https://opensky-network.org/api/flights/arrival"

    private val flightListLiveData = MutableLiveData<List<FlightModel>>(ArrayList())
    private val clickedFlightLiveData = MutableLiveData<FlightModel>()

    var icao: String = ""
    var isArrival: Boolean = false
    var depTime: Long = 0
    var arrTime: Long = 0

    fun getClickedFlightLiveData(): LiveData<FlightModel> {
        return clickedFlightLiveData
    }

    fun setClickedFlightLiveData(flight: FlightModel) {
        clickedFlightLiveData.value = flight
    }

    fun getFlightListLiveData(): LiveData<List<FlightModel>> {
        return flightListLiveData
    }

    fun setFlightListLiveData(flights: List<FlightModel>) {
        flightListLiveData.value = flights
    }

    fun doFlightListRequest() {
        viewModelScope.launch {
            val url = if(isArrival) ARRIVAL_API_URL else DEPARTURE_API_URL

            val requestParameters = HashMap<String, String>()
            requestParameters["begin"] = depTime.toString()
            requestParameters["end"] = arrTime.toString()
            requestParameters["airport"] = icao

            val result = withContext(Dispatchers.IO) {
                RequestManager.getSuspended(url, requestParameters)
            }

            if(result != null) {
                val flightList = ArrayList<FlightModel>()
                val parser = JsonParser()
                val jsonEl = parser.parse(result)
                for(flightObj in jsonEl.asJsonArray) {
                    flightList.add(Gson().fromJson(flightObj.asJsonObject, FlightModel::class.java))
                }

                setFlightListLiveData(flightList)
            } else {
                Log.e("REQUEST", "ERROR NO RESULT")
            }
        }
    }
}