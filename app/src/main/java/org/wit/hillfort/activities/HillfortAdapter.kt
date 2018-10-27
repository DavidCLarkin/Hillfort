package org.wit.hillfort.activities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.card_placemark.view.*
import org.wit.hillfort.R
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillfort.models.HillfortModel

interface HillfortListener
{
    fun onHillfortClick(hillfort: HillfortModel)
}

class HillfortAdapter constructor(private var hillforts: List<HillfortModel>, private val listener: HillfortListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>()
{

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder
    {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_placemark, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int)
    {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        fun bind(hillfort: HillfortModel, listener: HillfortListener)
        {
            itemView.viewTitle.text = hillfort.title
            itemView.viewDescription.text = hillfort.description
            for(image in hillfort.images) {
                itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, image))
            }
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
        }
    }

}