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
}