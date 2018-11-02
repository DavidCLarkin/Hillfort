package org.wit.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(var id: Long = 0,
                     var username: String = "",
                     var password: String = "",
                     var numberOfHillforts: Int = 0,
                     var numberVisited: Int = 0,
                     var hillforts: ArrayList<HillfortModel> = arrayListOf()) : Parcelable
{
    fun calcNumberOfHillorts() : Int
    {
        return hillforts.size
    }

    fun calcNumberVisited() : Int
    {
        var count = 0
        hillforts.forEach { if(it.visited) count++ }
        return count
    }
}

