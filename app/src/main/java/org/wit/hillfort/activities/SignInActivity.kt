package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R

class SignInActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signin.setOnClickListener()
        {
            print("clicked sign in")
            val intent = Intent(applicationContext, HillfortListActivity::class.java)
            startActivity(intent)
            finish()
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
