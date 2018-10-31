package org.wit.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserModel(var id: Long = 0,
                     var username: String = "",
                     var password: String = "",
                     var numberOfHillforts: Int = 0,
                     var numberVisited: Int = 0,
                     var hillforts: MutableList<HillfortModel> = arrayListOf()) : Parcelable