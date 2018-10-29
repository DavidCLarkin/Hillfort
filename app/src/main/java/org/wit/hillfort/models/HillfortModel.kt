package org.wit.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class HillfortModel(var id: Long = 0,
                         var title: String = "",
                         var description: String = "",
                         var images: MutableList<String> = mutableListOf<String>(),
                         var image: String = "",
                         var lat : Double = 0.0,
                         var long: Double = 0.0,
                         var zoom: Float = 0f,
                         var visited: Boolean = false,
                         var notes: String = "",
                         var date: String = "") : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var long: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable