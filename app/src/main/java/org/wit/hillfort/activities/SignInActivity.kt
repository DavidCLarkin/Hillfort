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
import java.lang.Exception


class SignInActivity : AppCompatActivity(), AnkoLogger
{

    lateinit var app: MainApp
    var success: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        app = application as MainApp
        val users: List<UserModel> = app.users.findAll()

        //If username and password are same as another users username and password, sign in.
        signin.setOnClickListener()
        {
            info("Users Size: ${users.size}")
            try
            {
                users
                    .forEach { user ->
                        if (username.text.toString().toLowerCase() == user.username && password.text.toString().toLowerCase() == user.password)
                        {
                            success = true
                            val intent = Intent(applicationContext, HillfortListActivity::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            finish()
                        }
                    }
                if(!success)
                {
                    val toast = Toast.makeText(applicationContext,
                        "Incorrect username or password", Toast.LENGTH_SHORT)
                    toast.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 0)
                    toast.duration = Toast.LENGTH_SHORT
                    toast.show()
                }
            }
            catch (e: Exception)
            {
                info { e.toString() }
            }

        }

        signup.setOnClickListener()
        {
            print("clicked sign up")
            val intent = Intent(applicationContext, SignUpActivity::class.java)
            startActivity(intent)
            //finish()
        }

        nosignin.setOnClickListener()
        {
            val intent = Intent(applicationContext, HillfortListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
