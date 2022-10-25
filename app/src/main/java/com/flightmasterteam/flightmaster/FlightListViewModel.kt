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

class FlightListViewModel: ViewModel() {
    private val flightListLiveData = MutableLiveData<List<FlightModel>>(ArrayList())

    fun getFlightListLiveData(): LiveData<List<FlightModel>> {
        return flightListLiveData
    }

    fun setFlightListLiveData(flights: List<FlightModel>) {
        flightListLiveData.value = flights
    }

    fun doRequest(begin: Long, end: Long, isArrival: Boolean, icao: String) {
        viewModelScope.launch {
            val url =
                if(isArrival) "https://opensky-network.org/api/flights/arrival"
                else "https://opensky-network.org/api/flights/departure"

            val requestParameters = HashMap<String, String>()
            requestParameters["begin"] = begin.toString()
            requestParameters["end"] = end.toString()
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