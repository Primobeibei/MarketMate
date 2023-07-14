package com.egci428.marketmate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.RecyclerView


class ShoppingListAdapter(private var itemObject: List<Item>):RecyclerView.Adapter<ShoppingListViewHolder>() {
    private lateinit var itemView: View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListViewHolder {
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)
        return ShoppingListViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return itemObject.size
    }

    override fun onBindViewHolder(holder: ShoppingListViewHolder, position: Int) {
        holder.itemName.setText(itemObject[position].name)
        holder.checkBox.isChecked = itemObject[position].checked
        holder.checkBox.setOnCheckedChangeListener {checkBox , isChecked ->
            itemObject[position].checked = isChecked
        }
        holder.itemName.doAfterTextChanged {
            itemObject[position].name = holder.itemName.text.toString()
        }
    }
    override fun getItemViewType(position: Int) = position

    override fun getItemId(position: Int) = position.toLong()

/*    private fun enableEdit(holder: ShoppingListViewHolder){
        holder.itemName.isEnabled = true
    }*/

    private fun disableEdit(holder: ShoppingListViewHolder){
        holder.itemName.isEnabled = false
    }

    fun getName(position: Int): String{
        return itemObject[position].name
    }

    fun getChecked(position: Int): String{
        return itemObject[position].checked.toString()
    }

    fun disable(){
        disableEdit(ShoppingListViewHolder(itemView))
    }


    /*fun enable(){
        enableEdit(ShoppingListViewHolder(itemView))
    }*/

}

class ShoppingListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
    var itemName: EditText = itemView.findViewById(R.id.itemName)
    var checkBox: CheckBox = itemView.findViewById(R.id.checkBox)
}