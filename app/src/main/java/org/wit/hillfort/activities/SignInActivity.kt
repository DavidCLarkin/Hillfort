package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.wit.hillfort.R
import org.wit.hillfort.models.UserModel
import android.view.Gravity
import android.widget.Toast
import org.wit.hillfort.helpers.*
import org.wit.hillfort.main.MainApp
import java.lang.Exception


class SignInActivity : AppCompatActivity(), AnkoLogger
{

    lateinit var app: MainApp
    var success: Boolean = false
    val PUBLIC_KEY_FILE = "pubkey.txt"
    val PRIVATE_KEY_FILE = "privkey.txt"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        app = application as MainApp
        val users: List<UserModel> = app.users.findAll()

        // If both public and private key files exist, then read the keys from them.
        if(exists(applicationContext, PUBLIC_KEY_FILE) && exists(applicationContext, PRIVATE_KEY_FILE)) {
            publicKey = read(applicationContext, PUBLIC_KEY_FILE)
            privateKey = read(applicationContext, PRIVATE_KEY_FILE)
            info { "reading file" }
        }
        else // If they don't exist, create new key value pairs. Should be realistically saved on server/db
        {
            generateKeyPair()
            write(applicationContext, PUBLIC_KEY_FILE, publicKey)
            write(applicationContext, PRIVATE_KEY_FILE, privateKey)
            info { "writing to file" }
        }


        //If username and password are same as another users username and password, sign in.
        signin.setOnClickListener()
        {
            try
            {
                users
                    .forEach { user ->
                        if (username.text.toString().toLowerCase() == user.username && password.text.toString().toLowerCase() == decrypt(user.password))
                        {
                            success = true
                            val intent = Intent(applicationContext, HillfortListView::class.java)
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
            val intent = Intent(applicationContext, HillfortListView::class.java)
            startActivity(intent)
            finish()
        }
    }

}
