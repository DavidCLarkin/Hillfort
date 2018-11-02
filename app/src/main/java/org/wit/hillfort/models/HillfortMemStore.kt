package org.wit.hillfort.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L

internal fun getId() : Long
{
    return lastId++
}

class HillfortMemStore : HillfortStore, AnkoLogger
{
    val hillforts = ArrayList<HillfortModel>()

    override fun findAll(): List<HillfortModel>
    {
        return hillforts
    }

    override fun create(hillfort: HillfortModel, user: UserModel)
    {
        hillfort.id = getId()
        hillforts.add(hillfort)
        logAll()
    }

    override fun update(hillfort: HillfortModel, user: UserModel)
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
            foundHillfort.notes = hillfort.notes
            foundHillfort.date = hillfort.date
            logAll()
        }
    }

    override fun delete(hillfort: HillfortModel, user: UserModel)
    {
        hillforts.remove(hillfort)
    }

    fun logAll()
    {
        hillforts.forEach { info("&{it}")}
    }
}