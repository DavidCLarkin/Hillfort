package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NavUtils
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.*

class SignUpActivity : AppCompatActivity(), AnkoLogger
{

    lateinit var app: MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        app = application as MainApp

        signUpToolbar.title = title
        setSupportActionBar(signUpToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        firstSignUp.setOnClickListener()
        {
            user.hillforts = HillfortJSONStore.hillforts
            user.username = usernameSignUp.text.toString()
            user.password = passwordSignUp.text.toString()
            user.id = generateUserRandomId()
            info("$user.username, $user.password")
            for (fort in user.hillforts)
            {
                info(fort.toString())
            }
            info("clicked sign up")

            app.users.create(user.copy())
            val intent = Intent(applicationContext, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) : Boolean
    {
        when (item.itemId)
        {
            R.id.home ->
            {
                info { "tested" }
                NavUtils.navigateUpFromSameTask(this)
                //finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
