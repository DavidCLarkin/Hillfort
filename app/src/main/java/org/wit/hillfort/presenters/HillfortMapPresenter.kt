package org.wit.hillfort.presenters

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.hillfort.activities.BaseView
import org.wit.hillfort.activities.HillfortMapView
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel

class HillfortMapPresenter(view: BaseView) : BasePresenter(view)
{

    fun doPopulateMap(map: GoogleMap, listToUse: List<HillfortModel>)
    {
        map.uiSettings.setZoomControlsEnabled(true)
        //map.setOnMarkerClickListener(view)
        listToUse.forEach {
            val loc = LatLng(it.lat, it.long)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }


    fun doMarkerSelected(marker: Marker)
    {
        val tag = marker.tag as Long
        val hillfort = app.hillforts.findById(tag)
        if (hillfort != null) view?.showHillfort(hillfort)
    }

    fun loadHillforts()
    {
        view?.showHillforts(app.hillforts.findAll())
    }


}