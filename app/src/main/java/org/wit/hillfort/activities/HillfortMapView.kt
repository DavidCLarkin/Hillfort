package org.wit.hillfort.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.hillfort.R
import kotlinx.android.synthetic.main.activity_hillfort_maps.*
import kotlinx.android.synthetic.main.content_hillfort_maps.*
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.presenters.HillfortMapPresenter

class HillfortMapView : BaseView(), GoogleMap.OnMarkerClickListener
{
    lateinit var app: MainApp
    lateinit var presenter: HillfortMapPresenter
    var user = UserModel()
    lateinit var map: GoogleMap
    var listToUse: List<HillfortModel> = arrayListOf() //list to show to specific users

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_maps)
        setSupportActionBar(mainToolbar)
        app = application as MainApp
        presenter = initPresenter(HillfortMapPresenter(this)) as HillfortMapPresenter

        user = intent.getParcelableExtra("user") as UserModel
        listToUse = app.hillforts.findAll().filter { it.usersID == user.id } //filter list by user id only

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            it.setOnMarkerClickListener(this)
            presenter.loadHillforts()
        }
    }

    override fun showHillfort(hillfort: HillfortModel)
    {
        currentTitle.text = hillfort.title
        currentDescription.text = hillfort.description
        if(hillfort.images.size > 0)
            imageView.setImageBitmap(readImageFromPath(this@HillfortMapView, hillfort.images[0]))

    }

    override fun onMarkerClick(marker: Marker): Boolean
    {
        presenter.doMarkerSelected(marker)
        return true
    }

    override fun showHillforts(hillforts: List<HillfortModel>)
    {
        presenter.doPopulateMap(map, listToUse)
        super.showHillforts(hillforts)
    }

    override fun onDestroy()
    {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory()
    {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause()
    {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume()
    {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle?)
    {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    /*fun configureMap()
    {
        map.setOnMarkerClickListener(this)
        map.uiSettings.setZoomControlsEnabled(true)
        listToUse.forEach {
            val loc = LatLng(it.lat, it.long)
            val options = MarkerOptions().title(it.title).position(loc)
            map.addMarker(options).tag = it.id
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean
    {
        val tag = marker.tag as Long
        val hillfort = app.hillforts.findById(tag)
        currentTitle.text = hillfort!!.title
        currentDescription.text = hillfort!!.description
        if(hillfort.images.size > 0)
            imageView.setImageBitmap(readImageFromPath(this@HillfortMapView, hillfort.images[0]))
        return true
    }
    */

}
