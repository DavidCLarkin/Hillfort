package org.wit.hillfort.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import org.wit.hillfort.R
import org.wit.hillfort.models.Location
import org.wit.hillfort.presenters.EditLocationPresenter

class EditLocationView : BaseView(), GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener
{

    lateinit var map: GoogleMap
    //var location = Location()
    lateinit var presenter: EditLocationPresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //location = intent.extras.getParcelable<Location>("location")
        presenter = EditLocationPresenter(this)
        mapFragment.getMapAsync{
            map = it
            map.setOnMarkerDragListener(this)
            map.setOnMarkerClickListener(this)
            presenter.initMap(map)
        }
    }

    override fun onMarkerDragStart(marker: Marker)
    {
    }

    override fun onMarkerDrag(marker: Marker)
    {
    }

    override fun onMarkerDragEnd(marker: Marker)
    {
        presenter.doUpdateLocation(marker.position.latitude, marker.position.longitude)
    }

    override fun onBackPressed()
    {
        presenter.doOnBackPressed()
    }

    override fun onMarkerClick(marker: Marker): Boolean
    {
        presenter.doUpdateMarker(marker)
        return false
    }
}
