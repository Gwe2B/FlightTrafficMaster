package com.flightmasterteam.flightmaster

const val TIME_INDEX = 0
const val LATITUDE_INDEX = 1
const val LONGITUDE_INDEX = 2
const val ALTITUDE_INDEX = 3
const val TRUE_TRACK_INDEX = 4
const val ON_GROUND_INDEX = 5

data class PathPointModel (
    val time: Long,
    val latitude: Double,
    val longitude: Double,
    val baroAltitude: Double,
    val trueTrack: Double,
    val onGround: Boolean
)
