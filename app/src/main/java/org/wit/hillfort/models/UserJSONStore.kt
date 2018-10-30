package org.wit.hillfort.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.*
import java.util.*

val USER_JSON_FILE = "users.json"
val user_GsonBuilder = GsonBuilder().setPrettyPrinting().create()
val user_listType = object: TypeToken<ArrayList<UserModel>>() {}.type


class UserJSONStore : UserStore, AnkoLogger
{

    val context: Context
    var users = mutableListOf<UserModel>()

    constructor(context: Context)
    {
        this.context = context
        if(exists(context, USER_JSON_FILE))
        {
            deserialize()
        }
    }

    override fun create(user: UserModel)
    {
        user.id = generateRandomId()
        users.add(user)
        serialize()
    }

    override fun findNumHillfortsVisited(user: UserModel): Int
    {
        user.hillforts.forEach{ if (it.visited) user.numberOfHillforts++ }
        return user.numberVisited
    }

    override fun findNumHillforts(user: UserModel): Int
    {
        return user.hillforts.size
    }



    /*override fun delete(user: UserModel)
    {
        users.remove(user)
        serialize()
    }
    */

    /*override fun update(hillfort: HillfortModel)
    {
        var foundHillfort: HillfortModel? = hillforts.find { hf -> hf.id == hillfort.id }
        if(foundHillfort != null)
        {
            foundHillfort.title = hillfort.title
            foundHillfort.description = hillfort.description
            foundHillfort.images = hillfort.images
            //foundHillfort.image = hillfort.image
            foundHillfort.lat = hillfort.lat
            foundHillfort.long = hillfort.long
            foundHillfort.zoom = hillfort.zoom
            foundHillfort.visited = hillfort.visited
            foundHillfort.notes = hillfort.notes
            foundHillfort.date = hillfort.date
        }

        serialize()
    }
    */

    private fun serialize()
    {
        val jsonString = user_GsonBuilder.toJson(users, user_listType)
        write(context, USER_JSON_FILE, jsonString)
    }

    private fun deserialize()
    {
        val jsonString = read(context, USER_JSON_FILE)
        users = Gson().fromJson(jsonString, listType)
    }
}