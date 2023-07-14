package com.egci428.marketmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ExpenseAdapter (private val expenseObjects: List<Expense>): RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rowmain, parent,false)
        return ExpenseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.draftView.text = expenseObjects[position].messageId
        holder.priceText.text = expenseObjects[position].price.toString()
    }



    override fun getItemCount(): Int {
        return expenseObjects.size
    }


    class ExpenseViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var draftView: TextView = itemView.findViewById(R.id.draftView)
        var priceText: TextView = itemView.findViewById(R.id.statusView)
    }
}
