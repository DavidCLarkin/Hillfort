package org.wit.hillfort.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.helpers.*
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
    var hillforts = mutableListOf<HillfortModel>()

    constructor(context: Context)
    {
        this.context = context
        if(exists(context, JSON_FILE))
        {
            deserialize()
        }
    }

    override fun findAll(): List<HillfortModel>
    {
        return hillforts
    }

    override fun create(hillfort: HillfortModel)
    {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort)
        serialize()
    }

    override fun delete(hillfort: HillfortModel)
    {
        hillforts.remove(hillfort)
        serialize()
    }

    override fun update(hillfort: HillfortModel)
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