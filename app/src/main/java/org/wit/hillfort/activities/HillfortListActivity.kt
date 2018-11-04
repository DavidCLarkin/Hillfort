package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import java.lang.Exception

class HillfortListActivity : AppCompatActivity(), HillfortListener, AnkoLogger
{
    lateinit var app: MainApp
    var user = UserModel()
    var listToUse: List<HillfortModel> = arrayListOf() //list to show to specific users

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        app = application as MainApp

        // Retrieve the user
        try
        {
            user = intent.getParcelableExtra("user") as UserModel
        }
        catch (e: Exception)
        {
        }

        toolbarMain.setTitle(R.string.hillfort_list)
        setSupportActionBar(toolbarMain)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        listToUse = app.hillforts.findAll().filter { it.usersID == user.id } //filter list by user id only
        recyclerView.adapter = HillfortAdapter(listToUse, this)

        loadHillforts()

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.toolbar_hillfort_list, menu) //set toolbar to custom made
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean
    {
        when (item?.itemId)
        {
            R.id.action_add ->
            {
                try
                {
                    val intent = Intent(applicationContext, HillfortActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    //finish()
                }
                catch (e: Exception) {}
            }
            R.id.action_logout ->
            {
                try
                {
                    val intent = Intent(applicationContext, SignInActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    finish()
                }
                catch (e: Exception) {}

            }
            R.id.action_settings ->
            {
                try
                {
                    val intent = Intent(applicationContext, SettingsActivity::class.java)
                    intent.putExtra("user", user)
                    startActivity(intent)
                    //finish()
                }
                catch (e:Exception) {}
            }
            //Other options to go here
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHillfortClick(hillfort: HillfortModel)
    {
        startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort).putExtra("user", user), 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        loadHillforts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun loadHillforts()
    {
        showHillforts(listToUse)
    }

    fun showHillforts(hillforts: List<HillfortModel>)
    {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}