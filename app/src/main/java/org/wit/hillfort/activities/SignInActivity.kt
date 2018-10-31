package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import org.wit.hillfort.R
import org.wit.hillfort.models.UserJSONStore
import org.wit.hillfort.models.UserModel
import android.view.Gravity
import android.widget.Toast
import org.wit.hillfort.main.MainApp


class SignInActivity : AppCompatActivity(), AnkoLogger
{

    //lateinit var app: MainApp
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        //app = application as MainApp

        //If username and password are same as another users username and password, sign in.
        signin.setOnClickListener()
        {
            info("Users Size: ${UserJSONStore.users.size}")
            for (user in UserJSONStore.users) //sometimes says it cant convert from HillfortModel to UserModel, but don't know why. Uninstalling and reinstalling app on emulator fixes this though.
                if (username.text.toString().toLowerCase() == user.username && password.text.toString().toLowerCase() == user.password)
                {
                    val intent = Intent(applicationContext, HillfortListActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else if(username.text.toString().toLowerCase() != user.username || password.text.toString().toLowerCase() != user.password)
                {
                    val toast = Toast.makeText(applicationContext,
                        "Incorrect username or password", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL, 0, 0)
                    toast.duration = Toast.LENGTH_LONG
                    toast.show()
                }
        }

        signup.setOnClickListener()
        {
            print("clicked sign up")
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
