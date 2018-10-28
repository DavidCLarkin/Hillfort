package org.wit.hillfort.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.wit.hillfort.R

class SigninActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signin.setOnClickListener {
            print("clicked sign in")
            val intent = Intent(applicationContext, HillfortActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


}
