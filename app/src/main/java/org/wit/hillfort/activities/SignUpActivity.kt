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
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.helpers.*
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.*
import java.lang.Exception

class SignUpActivity : AppCompatActivity(), AnkoLogger
{

    lateinit var app: MainApp
    var user = UserModel()
    var userValid : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        app = application as MainApp

        signUpToolbar.setTitle(R.string.create_account)
        setSupportActionBar(signUpToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //info { "Pub key Sign up : ${publicKey}Key" }
        //info {"Priv key Sign up :$privateKey"}

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

                userValid = !app.users.findAll().any { it -> user.username == it.username } //check if a user exists

                if(userValid)
                {
                    //Username and password can't be empty
                    if (!usernameSignUp.text.toString().isEmpty() && !passwordSignUp.text.toString().isEmpty())
                    {
                        //Username and password length must be greater than 4
                        if (usernameSignUp.text.toString().length > 4 && passwordSignUp.text.toString().length > 4)
                        {
                            //Password input has to be the same as password chosen
                            if (user.password == retypePassword.text.toString())
                            {
                                user.password = encrypt(user.password, publicKey)
                                //info { "Encrypted password: ${user.password}" }
                                app.users.create(user.copy())
                                val intent = Intent(applicationContext, SignInActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else
                                toast("Please make sure the two passwords are the same")
                        } else
                            toast("Username or Password length is too short - Must be at least 5 characters")
                    } else
                        toast("Username or Password fields cannot be empty")
                }
                else
                    toast("User already exists, try another Username")
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
