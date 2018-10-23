package org.wit.hillfort

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_hillfort.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


class HillfortActivity : AppCompatActivity(), AnkoLogger
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort)
        setSupportActionBar(findViewById(R.id.mainToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        info("Working")

        buttonAdd.setOnClickListener()
        {
            val hillfortTitle = hillfortTitle.text.toString()
            if (hillfortTitle.isNotEmpty()) {
                info("add Button Pressed: $hillfortTitle")
            }
            else {
                toast ("Please Enter a title")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean
    {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // actions on click menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_text ->
        {
            // User chose the "Print" item
            Toast.makeText(this,"Print action",Toast.LENGTH_LONG).show()
            true
        }
        android.R.id.home ->
        {
            Toast.makeText(this,"Home action",Toast.LENGTH_LONG).show()
            true
        }

        else ->
        {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}
