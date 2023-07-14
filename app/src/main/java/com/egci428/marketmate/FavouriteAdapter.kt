package com.egci428.marketmate

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class FavouriteAdapter (private val favouriteObjects: List<Favourite>): RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rowmain, parent,false)
        return FavouriteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {
        val context = holder.itemView.context
        val favouriteOB = favouriteObjects[position]
        holder.draftView.text = favouriteObjects[position].messageId
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ShoppingList::class.java)
            intent.putExtra("draft", favouriteOB.messageId)
            context.startActivity(intent)
        }
    }



    override fun getItemCount(): Int {
        return favouriteObjects.size
    }


    class FavouriteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var draftView: TextView = itemView.findViewById(R.id.draftView)
    }
}
