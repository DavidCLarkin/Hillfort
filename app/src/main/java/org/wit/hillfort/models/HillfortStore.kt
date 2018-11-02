package org.wit.hillfort.models

interface HillfortStore
{
    fun findAll() : List<HillfortModel>
    fun create(hillforts : HillfortModel, user: UserModel)
    fun update(hillfort: HillfortModel)
    fun delete(hillfort: HillfortModel)
}