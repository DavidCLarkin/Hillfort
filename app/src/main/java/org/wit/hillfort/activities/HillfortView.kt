package org.wit.hillfort.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.GoogleMap
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.presenters.HillfortPresenter
import java.lang.Exception
import java.util.*


class HillfortView : BaseView(), AnkoLogger
{

    var hillfort = HillfortModel()
    var user = UserModel()
    lateinit var map: GoogleMap
    lateinit var presenter: HillfortPresenter
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        mainToolbar.setTitle(R.string.create_hillfort)
        setSupportActionBar(mainToolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        presenter = initPresenter(HillfortPresenter(this)) as HillfortPresenter
        user = intent.getParcelableExtra("user") as UserModel

        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            presenter.doConfigureMap(map)
            map.setOnMapClickListener { presenter.doSetLocation() }
        }

        checkboxVisited.setOnClickListener()
        {
            presenter.doSetDate()
        }

        // Add a new hillfort or update one depending on the context
        buttonAdd.setOnClickListener()
        {
            if (hillfortTitle.text.toString().isEmpty())
            {
                toast(R.string.enter_hillfort_title)
            }
            else
            {
                presenter.doAddOrSave(hillfortTitle.text.toString(), description.text.toString(), notes.text.toString(), date.text.toString(), user)
            }
        }

        chooseImage.setOnClickListener {
            //showImagePicker(this, IMAGE_REQUEST)
            presenter.doSelectImage()
        }

        // Set the hillforts Location
        hillfortLocation.setOnClickListener {
            presenter.doSetLocation()
        }

        buttonDelete.setOnClickListener {
            presenter.doDelete(user)
            info("new size" + app.hillforts.findAll().size)
        }

    }

    override fun showHillfort(hillfort: HillfortModel)
    {
        hillfortTitle.setText(hillfort.title)
        description.setText(hillfort.description)
        date.setText(hillfort.date)
        notes.setText(hillfort.notes)
        checkboxVisited.isChecked = !hillfort.date.isEmpty() // setting checkbox ticked or unticked

        //Check size of images list, and show them accordingly
        for((i,image) in hillfort.images.withIndex())
        {
            info("size: ${hillfort.images.size}")
            if(i == 0) {
                hillfortImage0.setImageBitmap(readImageFromPath(this, hillfort.images.get(0)))
            }
            else if(i == 1) {
                hillfortImage0.setImageBitmap(readImageFromPath(this, hillfort.images.get(0)))
                hillfortImage1.setImageBitmap(readImageFromPath(this, hillfort.images.get(1)))
            }
            else if(i == 2) {
                hillfortImage0.setImageBitmap(readImageFromPath(this, hillfort.images.get(0)))
                hillfortImage1.setImageBitmap(readImageFromPath(this, hillfort.images.get(1)))
                hillfortImage2.setImageBitmap(readImageFromPath(this, hillfort.images.get(2)))
            }
            else if(i == 3) {
                hillfortImage0.setImageBitmap(readImageFromPath(this, hillfort.images.get(0)))
                hillfortImage1.setImageBitmap(readImageFromPath(this, hillfort.images.get(1)))
                hillfortImage2.setImageBitmap(readImageFromPath(this, hillfort.images.get(2)))
                hillfortImage3.setImageBitmap(readImageFromPath(this, hillfort.images.get(3)))
            }
        }

        lat.setText("%.6f".format(hillfort.lat))
        lng.setText("%.6f".format(hillfort.long))

        buttonAdd.setText(R.string.save_hillfort)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        if(presenter.edit)
            menu?.getItem(0)?.setVisible(true)
        return super.onCreateOptionsMenu(menu)
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) : Boolean
    {
        when (item?.itemId)
        {
            R.id.action_cancel ->
            {
                try
                {
                    presenter.doCancel()
                    //startActivity(intentFor<HillfortListView>().putExtra("user", user)) //return
                    //finish()
                }
                catch (e:Exception){
                    e.printStackTrace()
                }
            }

            R.id.action_delete ->
            {
                try
                {
                    presenter.doDelete(user)
                    info("new size"+app.hillforts.findAll().size)
                }
                catch (e:Exception){}
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null)
        {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume()
    {
        super.onResume()
        mapView.onResume()
        presenter.doResartLocationUpdates()
    }

    override fun onSaveInstanceState(outState: Bundle?)
    {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}
