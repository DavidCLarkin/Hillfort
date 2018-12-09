package org.wit.hillfort.presenters

import android.app.Activity
import android.content.Intent
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.hillfort.activities.MapsActivity
import org.wit.hillfort.models.Location

class MapsPresenter(val activity: MapsActivity)
{

    var location = Location()

    init
    {
        location = activity.intent.extras.getParcelable<Location>("location")
    }

    fun initMap(map: GoogleMap)
    {
        val loc = LatLng(location.lat, location.long)
        val options = MarkerOptions()
                .title("Hillfort")
                .snippet("GPS : " + loc.toString())
                .draggable(true)
                .position(loc)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, location.zoom))
    }

    fun doUpdateLocation(lat: Double, long: Double, zoom: Float)
    {
        location.lat = lat
        location.long = long
        location.zoom = zoom
    }

    fun doOnBackPressed()
    {
        val resultIntent = Intent()
        resultIntent.putExtra("location", location)
        activity.setResult(Activity.RESULT_OK, resultIntent)
        activity.finish()
    }

    fun doUpdateMarker(marker: Marker) {
        val loc = LatLng(location.lat, location.long)
        marker.setSnippet("GPS : " + loc.toString())
    }
}