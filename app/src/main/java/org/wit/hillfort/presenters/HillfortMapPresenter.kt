package org.wit.hillfort.presenters

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel

class HillfortMapPresenter(val view: HillfortkMapView) {

    var app: MainApp

    init {
        app = view.application as MainApp
    }


    fun doPopulateMap(map: GoogleMap, listToUse: List<HillfortModel>)
    {
        map.uiSettings.setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(view)
        listToUse.forEach {
            val loc = LatLng(it.lat, it.long)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }


    fun doMarkerSelected(marker: Marker) {
        val tag = marker.tag as Long
        val placemark = app.hillforts.findById(tag)
        if (placemark != null) view.showHillfort(hillfort)
    }

}