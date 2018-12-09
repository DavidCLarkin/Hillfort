package org.wit.hillfort.presenters

import org.jetbrains.anko.*
import org.wit.hillfort.activities.*
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel

class HillfortListPresenter(view: BaseView) : BasePresenter(view)
{

    fun getHillforts() = app.hillforts.findAll()

    fun getUserHillforts(user: UserModel) : List<HillfortModel>
    {
        return app.hillforts.findAll().filter { it.usersID == user.id } //filter list by user id only
    }

    fun doSettings(user: UserModel)
    {
        view?.startActivityForResult(view?.intentFor<SettingsActivity>()?.putExtra("user", user), 0)
    }

    fun doLogout(user: UserModel)
    {
        view?.startActivityForResult(view?.intentFor<SignInActivity>()?.putExtra("user", user), 0)
    }

    fun doAddHillfort(user: UserModel)
    {
        view?.startActivityForResult(view?.intentFor<HillfortView>()?.putExtra("user", user), 0)
    }

    fun doEditHillfort(hillfort: HillfortModel, user: UserModel)
    {
        view?.startActivityForResult(view?.intentFor<HillfortView>()?.putExtra("hillfort_edit", hillfort)?.putExtra("user", user), 0)
    }

    fun doShowHillfortsMap(user: UserModel)
    {
        view?.startActivityForResult(view?.intentFor<HillfortMapView>()?.putExtra("user", user), 0)
    }
}