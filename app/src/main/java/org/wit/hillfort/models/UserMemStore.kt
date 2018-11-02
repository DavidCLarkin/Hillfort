package org.wit.hillfort.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var userid = 0L

internal fun getUserId() : Long
{
    return userid++
}

class UserMemStore : UserStore, AnkoLogger
{
    var users = ArrayList<UserModel>()

    override fun create(user: UserModel)
    {
        user.id = getUserId()
        users.add(user)
    }
    override fun findNumHillforts(user: UserModel): Int
    {
        return user.hillforts.size
    }

    override fun findNumHillfortsVisited(user: UserModel): Int
    {
        user.hillforts.forEach{ if (it.visited) user.numberOfHillforts++ }
        return user.numberVisited
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
            foundUser.numberVisited = user.numberVisited
            foundUser.numberOfHillforts = user.numberOfHillforts
        }
    }

    override fun createHillfort(user: UserModel, hillfort: HillfortModel) {

    }

}