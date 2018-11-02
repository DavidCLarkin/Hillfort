package org.wit.hillfort.models

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.helpers.*
import java.lang.Exception
import java.util.*

val USER_JSON_FILE = "users.json"
val user_GsonBuilder = GsonBuilder().setPrettyPrinting().create()
val user_listType = object: TypeToken<ArrayList<UserModel>>() {}.type


fun generateUserRandomId(): Long
{
    return Random().nextLong()
}

class UserJSONStore : UserStore, AnkoLogger
{

    val context: Context
    companion object {
        var users = ArrayList<UserModel>()
    }

    constructor(context: Context)
    {
        this.context = context
        if(exists(context, USER_JSON_FILE))
        {
            try
            {
                deserialize()
            }
            catch (e: Exception)
            {

            }
        }
    }

    override fun create(user: UserModel)
    {
        user.id = generateUserRandomId()
        //user.numberVisited = findNumHillfortsVisited(user)
        //user.numberOfHillforts = findNumHillforts(user)
        users.add(user)
        serialize()
    }

    //not used
    override fun createHillfort(user: UserModel, hillfort: HillfortModel)
    {
        hillfort.id = generateRandomId()
        user.hillforts.add(hillfort)
        //update(user)
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

    override fun findAll(): List<UserModel>
    {
        return users
    }

    override fun update(user: UserModel)
    {
        var foundUser: UserModel? = users.find { it -> it.id == user.id }
        if(foundUser != null)
        {
            foundUser.id = user.id
            foundUser.username = user.username
            foundUser.password = user.password
            foundUser.hillforts = user.hillforts
            foundUser.numberVisited = user.calcNumberVisited()
            foundUser.numberOfHillforts = user.calcNumberOfHillorts()
        }

        serialize()
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
        info { "JSON: "+jsonString }
        write(context, USER_JSON_FILE, jsonString)
    }

    private fun deserialize()
    {
        val jsonString = read(context, USER_JSON_FILE)
        users = Gson().fromJson(jsonString, user_listType)
    }
}