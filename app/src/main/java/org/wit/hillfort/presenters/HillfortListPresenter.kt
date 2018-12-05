package org.wit.hillfort.presenters

import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.activities.HillfortActivity
import org.wit.hillfort.activities.HillfortListActivity
import org.wit.hillfort.activities.HillfortMapsActivity
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel

class PlacemarkListPresenter(val activity: HillfortListActivity)
{

    var app: MainApp

    init {
        app = activity.application as MainApp
    }

    fun getPlacemarks() = app.hillforts.findAll()

    fun doAddPlacemark() {
        activity.startActivityForResult<HillfortActivity>(0)
    }

    fun doEditPlacemark(placemark: HillfortModel) {
        activity.startActivityForResult(activity.intentFor<HillfortActivity>().putExtra("placemark_edit", placemark), 0)
    }

    fun doShowPlacemarksMap() {
        activity.startActivity<HillfortMapsActivity>()
    }
}