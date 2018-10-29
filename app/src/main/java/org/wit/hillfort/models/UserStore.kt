package org.wit.hillfort.models

interface UserStore
{
    fun findNumHillforts(user : UserModel) : Int
    fun findNumHillfortsVisited(user : UserModel) : Int
    fun create(user: UserModel)
}