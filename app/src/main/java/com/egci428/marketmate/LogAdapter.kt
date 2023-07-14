package com.egci428.marketmate

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView

class LogAdapter (private var logObject: List<Log>): RecyclerView.Adapter<LogViewHolder>() {
    private lateinit var logView: View
    var pos: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        logView = LayoutInflater.from(parent.context).inflate(R.layout.log, parent, false)
        return LogViewHolder(logView)
    }

    override fun getItemCount(): Int {
        return logObject.size
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.itemName.text = logObject[position].name
        holder.editText.doAfterTextChanged {
            logObject[position].date = holder.editText.text.toString()
        }
        /*holder.image.setOnClickListener {
            pos = position
            holder.image.setImageURI(logObject[position].uri.toUri())
        }*/
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

    fun getLogView(): View{
        return logView
    }
    fun getImage(holder: LogViewHolder,uri:Uri){
        holder.image.setImageURI(uri)
    }

}

class LogViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var itemName: TextView = itemView.findViewById(R.id.itemView)
    var editText: EditText = itemView.findViewById(R.id.editTextDate)
    var image: ImageButton = itemView.findViewById(R.id.imageButton2)
}