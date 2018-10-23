package org.wit.hillfort.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_hillfort.view.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.card_placemark.view.*
import org.wit.hillfort.R
import org.wit.hillfort.R.id.recyclerView
import org.wit.hillfort.main.MainApp
import org.wit.hillfort.models.HillfortModel

class HillfortListActivity : AppCompatActivity()
{
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = HillfortAdapter(app.hillforts)
    }
}

class HillfortAdapter constructor(private var hillforts: List<HillfortModel>) : RecyclerView.Adapter<HillfortAdapter.MainHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder
    {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_placemark, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int)
    {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        fun bind(hillfort: HillfortModel)
        {
            itemView.viewTitle.text = hillfort.title
            itemView.viewDescription.text = hillfort.description
        }
    }
}