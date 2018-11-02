package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.*
import java.lang.Exception

class SignUpActivity : AppCompatActivity(), AnkoLogger
{

    lateinit var app: MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        app = application as MainApp

        signUpToolbar.setTitle(R.string.create_account)
        setSupportActionBar(signUpToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        firstSignUp.setOnClickListener()
        {
            try
            {
                user.username = usernameSignUp.text.toString()
                user.password = passwordSignUp.text.toString()
                user.id = generateUserRandomId()

                val hillfortsFiltered: List<HillfortModel> = app.hillforts.findAll().filter { it.usersID == user.id }
                user.hillforts = hillfortsFiltered as ArrayList<HillfortModel>
                info("$user.username, $user.password")
                /*for (fort in user.hillforts)
            {
                info(fort.toString())
            }
            */
                info("clicked sign up")

                app.users.create(user.copy())

                val intent = Intent(applicationContext, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }
            catch (e:Exception) {}
        }
    }

    /*override fun onOptionsItemSelected(item: MenuItem) : Boolean
    {
        when (item.itemId)
        {
            R.id.home ->
            {
                try
                {
                    info { "tested" }
                    startActivity(intentFor<SignInActivity>())
                    finish()
                }
                catch (e: Exception) {}
            }
        }
        return super.onOptionsItemSelected(item)
    }
    */
}
