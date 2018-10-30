package org.wit.hillfort.main

import android.app.Application
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.models.*

class MainApp : Application(), AnkoLogger
{

    lateinit var hillforts : HillfortStore
    lateinit var users: UserStore

    override fun onCreate()
    {
        super.onCreate()
        users = UserJSONStore(applicationContext)
        hillforts = HillfortJSONStore(applicationContext)
        info("Hillfort started")
        //hillforts.add(HillfortModel("First", "Desc 1"))
        //hillforts.add(HillfortModel("Second","Desc 2"))
    }
}