package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.notification_template_custom_big.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.R
import org.wit.hillfort.models.HillfortModel
import org.wit.hillfort.models.UserModel

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

        user = intent.getParcelableExtra("user") as UserModel
        info { "current user: "+ user }

        toolbarMain.title = title
        setSupportActionBar(toolbarMain)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        listToUse = app.hillforts.findAll().filter { it.usersID == user.id } //filter list by user id only
        recyclerView.adapter = HillfortAdapter(listToUse, this)

        //var foundUser: UserModel? = app.users.findAll().find { it -> it.id == user.id }
        //recyclerView.adapter = HillfortAdapter(user.hillforts, this)
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
                val intent = Intent(applicationContext, HillfortActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            }
            R.id.action_logout ->
            {
                val intent = Intent(applicationContext, SignInActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
            }
            R.id.action_settings ->
            {
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                intent.putExtra("user", user)
                startActivity(intent)
                finish()
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
        //showHillforts(user.hillforts)
    }

    fun showHillforts(hillforts: List<HillfortModel>)
    {
        recyclerView.adapter = HillfortAdapter(hillforts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }
}