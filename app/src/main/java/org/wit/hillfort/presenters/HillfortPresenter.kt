package org.wit.hillfort.presenters

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.activities.*
import org.wit.hillfort.helpers.*
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.UserModel
import java.util.*

class HillfortPresenter(view: BaseView) : BasePresenter(view), AnkoLogger
{
    var map: GoogleMap? = null
    val locationRequest = createDefaultLocationRequest()
    var locationService: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(view)
    var hillfort = HillfortModel()
    var defaultLocation = Location(52.245696, -7.139102, 15f)
    var edit = false;

    init {
        //app = activity.application as MainApp
        if (view.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = view.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            view.showHillfort(hillfort)
        }
        else
        {
            if(checkLocationPermissions(view))
            {
                doSetCurrentLocation()
                //hillfort.lat = defaultLocation.lat
                //hillfort.long = defaultLocation.long
            }
        }
    }

    fun doConfigureMap(m: GoogleMap)
    {
        map = m
        locationUpdate(hillfort.lat, hillfort.long)
    }

    @SuppressLint("MissingPermission")
    fun doResartLocationUpdates()
    {
        var locationCallback = object : LocationCallback()
        {
            override fun onLocationResult(locationResult: LocationResult?)
            {
                if (locationResult != null && locationResult.locations != null)
                {
                    val l = locationResult.locations.last()
                    locationUpdate(l.latitude, l.longitude)
                }
            }
        }
        if (!edit) {
            locationService.requestLocationUpdates(locationRequest, locationCallback, null)
        }
    }

    fun locationUpdate(lat: Double, long: Double)
    {
        hillfort.lat = lat
        hillfort.long = long
        hillfort.zoom = 15f
        map?.clear()
        map?.uiSettings?.setZoomControlsEnabled(true)
        val options = MarkerOptions().title(hillfort.title).position(LatLng(hillfort.lat, hillfort.long))
        map?.addMarker(options)
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(hillfort.lat, hillfort.long), hillfort.zoom))
        view?.showHillfort(hillfort)
    }

    override fun doRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        if (isPermissionGranted(requestCode, grantResults))
        {
            doSetCurrentLocation()
        }
        else
        {
            // permissions denied, so use the default location
            locationUpdate(defaultLocation.lat, defaultLocation.long)
        }
    }

    fun doAddOrSave(title: String, description: String, notes: String, date: String, user: UserModel)
    {
        hillfort.title = title
        hillfort.description = description
        hillfort.notes = notes
        hillfort.date = date

        if (hillfort.title.isEmpty())
        {
            info { "please enter title" }
        }
        else
        {
            if(edit)
            {
                app.hillforts.update(hillfort.copy(), user.copy())
            }
            else
            {
                app.hillforts.create(hillfort.copy(), user.copy())

            }
            view?.startActivityForResult(view?.intentFor<HillfortListView>()?.putExtra("user", user), 0)
            //activity.finish()
        }
    }

    fun doCancel()
    {
        view?.finish()
    }

    fun doDelete(user: UserModel)
    {
        app.hillforts.delete(hillfort, user.copy())
        view?.startActivityForResult(view?.intentFor<HillfortListView>()?.putExtra("user", user), 0)
        //activity.finish()
    }

    fun doSelectImage()
    {
        showImagePicker(view!!, IMAGE_REQUEST)
    }

    fun doSetDate()
    {
        if (view!!.checkboxVisited.isChecked)
        {
            hillfort.visited = true

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(view!!, DatePickerDialog.OnDateSetListener{ DatePicker, mYear, mMonth, mDay ->
                view?.date?.setText("" + mDay + "/" + mMonth + "/" + mYear)
            }, year, month, day )

            dpd.show()
            hillfort.date = view?.date?.text.toString()
        }
        else
        {
            hillfort.visited = false
            view?.date?.setText("")
            hillfort.date = ""
        }
    }

    fun doSetLocation()
    {
        view?.navigateTo(VIEW.LOCATION, LOCATION_REQUEST, "location", Location(hillfort.lat, hillfort.long, hillfort.zoom))
        /*if (hillfort.zoom != 0f)
        {
            defaultLocation.lat = hillfort.lat
            defaultLocation.long = hillfort.long
            location.zoom = hillfort.zoom
        }
        view?.startActivityForResult(view?.intentFor<EditLocationView>()?.putExtra("location", location), LOCATION_REQUEST)
        */
    }

    @SuppressLint("MissingPermission")
    fun doSetCurrentLocation()
    {
        locationService.lastLocation.addOnSuccessListener {
            locationUpdate(it.latitude, it.longitude)
        }
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent)
    {
        when(requestCode)
        {
            IMAGE_REQUEST ->
            {
                if(data != null)
                {
                    if(hillfort.images.size < 4)
                        hillfort.images.add(data.data.toString())
                    else
                        info("Full")

                    for((i,image) in hillfort.images.withIndex())
                    {
                        info("size: ${hillfort.images.size}")
                        if(i == 0) {
                            view?.hillfortImage0?.setImageBitmap(readImageFromPath(view!!, hillfort.images.get(0)))
                        }
                        else if(i == 1) {
                            view?.hillfortImage0?.setImageBitmap(readImageFromPath(view!!, hillfort.images.get(0)))
                            view?.hillfortImage1?.setImageBitmap(readImageFromPath(view!!, hillfort.images.get(1)))
                        }
                        else if(i == 2) {
                            view?.hillfortImage0?.setImageBitmap(readImageFromPath(view!!, hillfort.images.get(0)))
                            view?.hillfortImage1?.setImageBitmap(readImageFromPath(view!!, hillfort.images.get(1)))
                            view?.hillfortImage2?.setImageBitmap(readImageFromPath(view!!, hillfort.images.get(2)))
                        }
                        else if(i == 3) {
                            view?.hillfortImage0?.setImageBitmap(readImageFromPath(view!!, hillfort.images.get(0)))
                            view?.hillfortImage1?.setImageBitmap(readImageFromPath(view!!, hillfort.images.get(1)))
                            view?.hillfortImage2?.setImageBitmap(readImageFromPath(view!!, hillfort.images.get(2)))
                            view?.hillfortImage3?.setImageBitmap(readImageFromPath(view!!, hillfort.images.get(3)))
                        }
                        //break
                    }

                    view?.chooseImage?.setText(R.string.change_hillfort_image)
                }
                view?.showHillfort(hillfort)
            }
            LOCATION_REQUEST ->
            {
                if(data != null)
                {
                    val location = data.extras.getParcelable<Location>("location")
                    hillfort.lat = location.lat
                    hillfort.long = location.long
                    hillfort.zoom = location.zoom
                    locationUpdate(hillfort.lat, hillfort.long)
                }
            }
        }
    }
}
