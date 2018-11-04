package org.wit.hillfort.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.intentFor
import org.wit.hillfort.R
import org.wit.hillfort.helpers.decrypt
import org.wit.hillfort.helpers.encrypt
import org.wit.hillfort.helpers.publicKey
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.UserModel

class SettingsActivity : AppCompatActivity(), AnkoLogger
{
    lateinit var app : MainApp
    var user = UserModel()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        app = application as MainApp

        user = intent.getParcelableExtra("user")

        settingsToolbar.setTitle(R.string.settings)
        setSupportActionBar(settingsToolbar)

        usernameField.setText(user.username)
        passwordField.setText(decrypt(user.password))
        numbOfHillforts.text = numbOfHillforts.text.toString() + " ${user.hillforts.size}"
        numbOfHillfortsVisited.text = numbOfHillfortsVisited.text.toString()+ " ${user.numberVisited}"


        saveSettings.setOnClickListener {
            user.username = usernameField.text.toString()
            user.password = encrypt(passwordField.text.toString(), publicKey) //encrypt if changed

            app.users.update(user.copy())

            startActivityForResult(intentFor<HillfortListActivity>().putExtra("user", user), 1) //return to main screen
            setResult(AppCompatActivity.RESULT_OK)

        }

        cancelSettings.setOnClickListener {
            startActivity(intentFor<HillfortListActivity>().putExtra("user", user))
        }

    }

}
