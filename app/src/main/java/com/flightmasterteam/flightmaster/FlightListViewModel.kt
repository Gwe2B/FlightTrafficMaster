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
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

private const val DEPARTURE_API_URL = "https://opensky-network.org/api/flights/departure"
private const val ARRIVAL_API_URL = "https://opensky-network.org/api/flights/arrival"
private const val AIRPLANE_TRACK_API_URL = "https://opensky-network.org/api/tracks/all"

class FlightListViewModel: ViewModel() {
    private val flightListLiveData = MutableLiveData<List<FlightModel>>(ArrayList())
    private val clickedFlightLiveData = MutableLiveData<FlightModel>()
    private val clickedFlightTrackLiveData = MutableLiveData<TrackModel>()
    private val errorLiveData = MutableLiveData<String?>()

    var icao: String = ""
    var isArrival: Boolean = false
    var depTime: Long = 0
    var arrTime: Long = 0

    fun getErrorLiveData(): LiveData<String?> {
        return errorLiveData
    }

    fun setErrorLiveData(msg: String?) {
        errorLiveData.value = msg
    }

    fun getClickedFlightLiveData(): LiveData<FlightModel> {
        return clickedFlightLiveData
    }

    fun setClickedFlightLiveData(flight: FlightModel) {
        clickedFlightLiveData.value = flight
    }

    fun getFlightListLiveData(): LiveData<List<FlightModel>> {
        return flightListLiveData
    }

    private fun setFlightListLiveData(flights: List<FlightModel>) {
        flightListLiveData.value = flights
    }

    fun getClickedFlightTrackLiveData(): LiveData<TrackModel> {
        return clickedFlightTrackLiveData
    }

    private fun setFlightTrackLiveData(track: TrackModel) {
        clickedFlightTrackLiveData.value = track
    }

    fun doAirplaneTrackRequest() {
        viewModelScope.launch {
            val selectedFlight = getClickedFlightLiveData().value!!
            val requestParameters = HashMap<String, String>()
            requestParameters["icao24"] = selectedFlight.icao24
            requestParameters["time"] = selectedFlight.firstSeen.toString()

            try {
                val result = withContext(Dispatchers.IO) {
                    RequestManager.getSuspended(AIRPLANE_TRACK_API_URL, requestParameters)
                }

                if (result != null) {
                    val jObject = JSONObject(result)
                    val jArray = jObject.getJSONArray("path")
                    val jArraySize = jArray.length() - 1
                    val pathArr = ArrayList<PathPointModel>()

                    for (i in 0..jArraySize) {
                        try {
                            val el = jArray.getJSONArray(i)
                            pathArr.add(
                                PathPointModel(
                                    el.getLong(TIME_INDEX),
                                    el.getDouble(LATITUDE_INDEX),
                                    el.getDouble(LONGITUDE_INDEX),
                                    el.getDouble(ALTITUDE_INDEX),
                                    el.getDouble(TRUE_TRACK_INDEX),
                                    el.getBoolean(ON_GROUND_INDEX)
                                )
                            )
                        } catch (err: JSONException) {
                            Log.e("FlightTrack", "Error at index $i of the array")
                            Log.e("FlightTrack", err.toString())
                        }
                    }

                    val tracker = TrackModel(
                        jObject.getString("icao24"),
                        jObject.getLong("startTime"),
                        jObject.getLong("endTime"),
                        jObject.getString("callsign"),
                        pathArr
                    )

                    setFlightTrackLiveData(tracker)
                } else {
                    Log.e("REQUEST", "ERROR NO RESULT")
                }
            } catch(e: IOException) {
                setErrorLiveData("Unable to retrieve the data from the API.\nPlease try again later.")
            }
        }
    }

    fun doFlightListRequest() {
        viewModelScope.launch {
            val url = if(isArrival) ARRIVAL_API_URL else DEPARTURE_API_URL

            val requestParameters = HashMap<String, String>()
            requestParameters["begin"] = depTime.toString()
            requestParameters["end"] = arrTime.toString()
            requestParameters["airport"] = icao

            try {
                val result = withContext(Dispatchers.IO) {
                    RequestManager.getSuspended(url, requestParameters)
                }

                if (result != null) {
                    val flightList = ArrayList<FlightModel>()
                    val parser = JsonParser()
                    val jsonEl = parser.parse(result)
                    for (flightObj in jsonEl.asJsonArray) {
                        flightList.add(
                            Gson().fromJson(
                                flightObj.asJsonObject,
                                FlightModel::class.java
                            )
                        )
                    }

                    setFlightListLiveData(flightList)
                } else {
                    Log.e("REQUEST", "ERROR NO RESULT")
                }
            } catch(e: IOException) {
                setErrorLiveData("Unable to retrieve the data from the API.\nPlease try again later.")
            }
        }
    }
}