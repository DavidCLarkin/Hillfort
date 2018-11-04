package org.wit.hillfort.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.UserModel
import java.lang.Exception
import java.util.*


class HillfortActivity : AppCompatActivity(), AnkoLogger
{
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    var edit = false
    var hillfort = HillfortModel()
    var user = UserModel()
    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        app = application as MainApp

        mainToolbar.setTitle(R.string.create_hillfort)
        setSupportActionBar(mainToolbar)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        user = intent.getParcelableExtra("user") as UserModel

        info("Working")

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

        //If edit, get the object and set view values
        if(intent.hasExtra("hillfort_edit"))
        {
            edit = true
            hillfort = intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            mainToolbar.setTitle(R.string.edit_hillfort)


            // Set the variables to the hillforts data
            hillfortTitle.setText(hillfort.title)
            description.setText(hillfort.description)
            checkboxVisited.isChecked = hillfort.visited
            notes.setText(hillfort.notes)
            date.setText(hillfort.date)

            buttonAdd.setText(R.string.save_hillfort)

            for((i,image) in hillfort.images.withIndex())
            {
                info("size: ${hillfort.images.size}")
                if(i == 0) {
                    hillfortImage0.setImageBitmap(readImageFromPath(this, hillfort.images.get(0)))
                }
                else if(i == 1) {
                    hillfortImage1.setImageBitmap(readImageFromPath(this, hillfort.images.get(1)))
                }
                else if(i == 2) {
                    hillfortImage2.setImageBitmap(readImageFromPath(this, hillfort.images.get(2)))
                }
                else if(i == 3) {
                    hillfortImage3.setImageBitmap(readImageFromPath(this, hillfort.images.get(3)))
                }
            }

            if(!hillfort.images.isEmpty())
            {
                chooseImage.setText("Change Hillfort images")
            }
        }

        // When checkbox is checked, display a date picker and set this date.
        checkboxVisited.setOnClickListener()
        {
            info("Clciked")
            if (checkboxVisited.isChecked)
            {
                hillfort.visited = true

                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{DatePicker, mYear, mMonth, mDay ->
                    date.setText("" + mDay + "/" + mMonth + "/" + mYear)
                }, year, month, day )

                dpd.show()
                hillfort.date = date.text.toString()
            }
            else
            {
                hillfort.visited = false
                date.setText("")
                hillfort.date = ""
            }
        }


        // Add a new hillfort or update one depending on the context
        buttonAdd.setOnClickListener()
        {
            hillfort.title = hillfortTitle.text.toString()
            hillfort.description = description.text.toString()
            hillfort.notes = notes.text.toString()
            hillfort.date = date.text.toString()

            if (hillfort.title.isEmpty())
            {
                toast(R.string.enter_hillfort_title)
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

                startActivity(intentFor<HillfortListActivity>().putExtra("user", user)) //return to main screen
                setResult(AppCompatActivity.RESULT_OK)
            }
        }

        chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
            //recreate()
        }

        // Set the hillforts Location
        hillfortLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if(hillfort.zoom != 0f)
            {
                location.lat = hillfort.lat
                location.long = hillfort.long
                location.zoom = hillfort.zoom
            }
            startActivityForResult(intentFor<MapsActivity>().putExtra("location", location).putExtra("user", user), LOCATION_REQUEST)
        }

        buttonDelete.setOnClickListener {
            app.hillforts.delete(hillfort.copy(), user.copy())
            startActivity(intentFor<HillfortListActivity>().putExtra("user", user)) //return to main screen
            info("Delete Clicked")
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
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
                    startActivity(intentFor<HillfortListActivity>().putExtra("user", user)) //return
                    finish()
                }
                catch (e:Exception){}
            }

            R.id.action_delete ->
            {
                try
                {
                    app.hillforts.delete(hillfort.copy(), user.copy())
                    startActivity(intentFor<HillfortListActivity>().putExtra("user", user)) //return to main screen
                }
                catch (e:Exception){}
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            IMAGE_REQUEST ->
            {
                if(data != null)
                {
                    if(hillfort.images.size < 4)
                        hillfort.images.add(data.getData().toString())
                    else
                        info("Full")

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
                        //break
                    }

                    chooseImage.setText(R.string.change_hillfort_image)
                }
            }
            LOCATION_REQUEST ->
            {
                if(data != null)
                {
                    val location = data.extras.getParcelable<Location>("location")
                    hillfort.lat = location.lat
                    hillfort.long = location.long
                    hillfort.zoom = location.zoom
                }
            }
        }
    }
}
