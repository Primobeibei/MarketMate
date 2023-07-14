package com.egci428.marketmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView

class ExpiryListAdapter (private var logObject: List<Log>): RecyclerView.Adapter<ExpiryListViewHolder>() {
    private lateinit var logView: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpiryListViewHolder {
        logView = LayoutInflater.from(parent.context).inflate(R.layout.expiryrow, parent, false)
        return ExpiryListViewHolder(logView)
    }

    override fun getItemCount(): Int {
        return logObject.size
    }

    override fun onBindViewHolder(holder: ExpiryListViewHolder, position: Int) {
        holder.itemName.text = logObject[position].name
        holder.date.text = logObject[position].date
        holder.image.setImageURI(logObject[position].uri.toUri())
    }

    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

    fun getName(position: Int): String{
        return logObject[position].name
    }

    fun getDate(position: Int): String{
        return logObject[position].date
    }

    fun getUri(position: Int): String{
        return logObject[position].uri
    }

}

class ExpiryListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var itemName: TextView = itemView.findViewById(R.id.nameView)
    var date: TextView = itemView.findViewById(R.id.dateView)
    var image: ImageView = itemView.findViewById(R.id.imageView2)
}