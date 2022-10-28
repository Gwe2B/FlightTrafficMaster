package com.flightmasterteam.flightmaster

data class TrackModel(
    val icao24: String,
    val startTime: Long,
    val endTime: Long,
    val callsign: String,
    val path: List<PathPointModel>
)