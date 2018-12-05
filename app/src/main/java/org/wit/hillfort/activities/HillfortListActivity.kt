package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel
import org.wit.hillfort.presenters.HillfortListPresenter
import java.lang.Exception

class HillfortListActivity : AppCompatActivity(), HillfortListener, AnkoLogger
{
    var user = UserModel()
    var listToUse: List<HillfortModel> = arrayListOf() //list to show to specific users
    lateinit var presenter: HillfortListPresenter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)

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

        presenter = HillfortListPresenter(this)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        listToUse = presenter.getUserHillforts(user)
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
                    presenter.doAddHillfort(user)
                }
                catch (e: Exception) {}
            }
            R.id.action_logout ->
            {
                try
                {
                    presenter.doLogout(user)
                }
                catch (e: Exception) {}

            }
            R.id.action_settings ->
            {
                try
                {
                    presenter.doSettings(user)
                }
                catch (e:Exception) {}
            }
            R.id.item_map ->
            {
                presenter.doShowHillfortsMap(user)
            }

            //Other options to go here
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onHillfortClick(hillfort: HillfortModel)
    {
        presenter.doEditHillfort(hillfort, user)
        //startActivityForResult(intentFor<HillfortActivity>().putExtra("hillfort_edit", hillfort).putExtra("user", user), 0)
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