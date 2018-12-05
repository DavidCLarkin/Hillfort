package org.wit.hillfort.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.*
import java.lang.Exception
import java.util.*

val JSON_FILE = "hillforts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object: TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

fun generateRandomId(): Long
{
    return Random().nextLong()
}

class HillfortJSONStore : HillfortStore, AnkoLogger
{
    val context: Context
    companion object {
        var hillforts = ArrayList<HillfortModel>()
    }

    constructor(context: Context)
    {
        this.context = context
        if(exists(context, JSON_FILE))
        {
            try {

                deserialize()
            }
            catch (e: Exception)
            {

            }
        }
    }

    override fun findAll(): List<HillfortModel>
    {
        return hillforts
    }

    override fun findById(id:Long) : HillfortModel?
    {
        val foundPlacemark: HillfortModel? = hillforts.find { it.id == id }
        return foundPlacemark
    }

    override fun create(hillfort: HillfortModel, user: UserModel)
    {
        hillfort.id = generateRandomId()
        hillfort.usersID = user.id
        user.hillforts.add(hillfort)
        user.numberOfHillforts++
        if (hillfort.visited)
           user.numberVisited++
        hillforts.add(hillfort)
        serialize()
        deserialize()
    }

    override fun delete(hillfort: HillfortModel, user: UserModel)
    {
        user.hillforts.remove(hillfort)
        hillforts.remove(hillfort)
        serialize()
    }

    override fun update(hillfort: HillfortModel, user: UserModel)
    {
        var foundHillfort: HillfortModel? = hillforts.find { hf -> hf.id == hillfort.id }
        var userHillfort: HillfortModel? = user.hillforts.find { userHillfort -> userHillfort.id == hillfort.id }
        if(foundHillfort != null && userHillfort != null)
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
            foundHillfort.usersID = hillfort.usersID

            userHillfort.title = hillfort.title
            userHillfort.description = hillfort.description
            userHillfort.images = hillfort.images
            //foundHillfort.image = hillfort.image
            userHillfort.lat = hillfort.lat
            userHillfort.long = hillfort.long
            userHillfort.zoom = hillfort.zoom
            userHillfort.visited = hillfort.visited
            userHillfort.notes = hillfort.notes
            userHillfort.date = hillfort.date
            userHillfort.usersID = hillfort.usersID

        }

        serialize()
        deserialize()
    }

    private fun serialize()
    {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize()
    {
        val jsonString = read(context, JSON_FILE)
        hillforts = Gson().fromJson(jsonString, listType)
    }
}