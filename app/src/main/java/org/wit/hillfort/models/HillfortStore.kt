package org.wit.hillfort.models

interface HillfortStore
{
    fun findAll() : List<HillfortModel>
    fun create(hillforts : HillfortModel, user: UserModel)
    fun update(hillfort: HillfortModel, user: UserModel)
    fun delete(hillfort: HillfortModel, user: UserModel)
}