package org.wit.hillfort.presenters

import android.content.Intent
import org.jetbrains.anko.*
import org.wit.hillfort.activities.*
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel

class HillfortListPresenter(val activity: HillfortListActivity) : AnkoLogger
{

    var app: MainApp

    init {
        app = activity.application as MainApp
    }

    fun getHillforts() = app.hillforts.findAll()

    fun getUserHillforts(user: UserModel) : List<HillfortModel>
    {
        return app.hillforts.findAll().filter { it.usersID == user.id } //filter list by user id only
    }

    fun doSettings(user: UserModel)
    {
        info { "settings" }
        activity.startActivityForResult(activity.intentFor<SettingsActivity>().putExtra("user", user), 0)
    }

    fun doLogout(user: UserModel)
    {
        activity.startActivityForResult(activity.intentFor<SignInActivity>().putExtra("user", user), 0)
    }

    fun doAddHillfort(user: UserModel)
    {
        activity.startActivityForResult(activity.intentFor<HillfortActivity>().putExtra("user", user), 0)
    }

    fun doEditHillfort(hillfort: HillfortModel, user: UserModel)
    {
        activity.startActivityForResult(activity.intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort).putExtra("user", user), 0)
    }

    fun doShowHillfortsMap(user: UserModel)
    {
        activity.startActivityForResult(activity.intentFor<HillfortMapsActivity>().putExtra("user", user), 0)
    }
}