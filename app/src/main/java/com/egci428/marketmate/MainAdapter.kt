package com.egci428.marketmate

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MainAdapter (private val mainObjects: ArrayList<MainItem>): RecyclerView.Adapter<MainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rowmain, parent, false)
        return MainViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val context = holder.itemView.context
        val favouriteOB = mainObjects[position]
        holder.draftView.text = mainObjects[position].messageId
        if (!mainObjects[position].status) {
            holder.statusView.text = "On progress of shopping"
        } else {
            holder.statusView.text = "Done shopping"
        }
        holder.itemView.setOnClickListener {
            if (mainObjects[position].status) {
                val intent = Intent(context, ExpiryLog::class.java)
                intent.putExtra("draft", favouriteOB.messageId)
                context.startActivity(intent)
            } else {
                val intent = Intent(context, ShoppingList::class.java)
                intent.putExtra("draft", favouriteOB.messageId)
                context.startActivity(intent)
            }
        }
    }


    override fun getItemCount(): Int {
        return mainObjects.size
    }
}

class MainViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var draftView: TextView = itemView.findViewById(R.id.draftView)
        var statusView: TextView = itemView.findViewById(R.id.statusView)
}

