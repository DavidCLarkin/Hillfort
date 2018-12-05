package org.wit.hillfort.presenters

import android.app.DatePickerDialog
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.R.id.hillfortTitle
import org.wit.hillfort.helpers.showImagePicker
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.activities.HillfortActivity
import org.wit.hillfort.activities.HillfortListActivity
import org.wit.hillfort.activities.MapsActivity
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.Location
import org.wit.hillfort.models.UserModel
import java.util.*

class HillfortPresenter(val activity: HillfortActivity) : AnkoLogger, AppCompatActivity()
{

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    var hillfort = HillfortModel()
    var location = Location(52.245696, -7.139102, 15f)
    var app: MainApp
    var edit = false;

    init {
        app = activity.application as MainApp
        if (activity.intent.hasExtra("hillfort_edit")) {
            edit = true
            hillfort = activity.intent.extras.getParcelable<HillfortModel>("hillfort_edit")
            activity.showHillfort(hillfort)
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
            activity.toast(R.string.enter_hillfort_title)
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
            activity.startActivityForResult(activity.intentFor<HillfortListActivity>().putExtra("user", user), 0)
            //activity.finish()
        }
    }

    fun doCancel()
    {
        activity.finish()
    }

    fun doDelete(user: UserModel)
    {
        app.hillforts.delete(hillfort, user.copy())
        activity.startActivityForResult(activity.intentFor<HillfortListActivity>().putExtra("user", user), 0)
        //activity.finish()
    }

    fun doSelectImage()
    {
        showImagePicker(activity, IMAGE_REQUEST)
    }

    fun doSetDate()
    {
        if (activity.checkboxVisited.isChecked)
        {
            hillfort.visited = true

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener{ DatePicker, mYear, mMonth, mDay ->
                activity.date.setText("" + mDay + "/" + mMonth + "/" + mYear)
            }, year, month, day )

            dpd.show()
            hillfort.date = activity.date.text.toString()
        }
        else
        {
            hillfort.visited = false
            activity.date.setText("")
            hillfort.date = ""
        }
    }

    fun doSetLocation()
    {
        if (hillfort.zoom != 0f)
        {
            location.lat = hillfort.lat
            location.long = hillfort.long
            location.zoom = hillfort.zoom
        }
        activity.startActivityForResult(activity.intentFor<MapsActivity>().putExtra("location", location), LOCATION_REQUEST)
    }

    fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent)
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
                            activity.hillfortImage0.setImageBitmap(readImageFromPath(activity, hillfort.images.get(0)))
                        }
                        else if(i == 1) {
                            activity.hillfortImage0.setImageBitmap(readImageFromPath(activity, hillfort.images.get(0)))
                            activity.hillfortImage1.setImageBitmap(readImageFromPath(activity, hillfort.images.get(1)))
                        }
                        else if(i == 2) {
                            activity.hillfortImage0.setImageBitmap(readImageFromPath(activity, hillfort.images.get(0)))
                            activity.hillfortImage1.setImageBitmap(readImageFromPath(activity, hillfort.images.get(1)))
                            activity.hillfortImage2.setImageBitmap(readImageFromPath(activity, hillfort.images.get(2)))
                        }
                        else if(i == 3) {
                            activity.hillfortImage0.setImageBitmap(readImageFromPath(activity, hillfort.images.get(0)))
                            activity.hillfortImage1.setImageBitmap(readImageFromPath(activity, hillfort.images.get(1)))
                            activity.hillfortImage2.setImageBitmap(readImageFromPath(activity, hillfort.images.get(2)))
                            activity.hillfortImage3.setImageBitmap(readImageFromPath(activity, hillfort.images.get(3)))
                        }
                        //break
                    }

                    activity.chooseImage.setText(R.string.change_hillfort_image)
                }
                activity.showHillfort(hillfort)
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
