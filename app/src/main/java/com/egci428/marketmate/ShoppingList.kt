package com.egci428.marketmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

class ShoppingList : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var remainView: TextView
    private lateinit var editText: TextView
    private lateinit var plaintext: EditText
    private lateinit var confirmBtn: Button
    private lateinit var favBtn: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var editBtn: ImageButton
    private lateinit var adapter: ShoppingListAdapter
    private lateinit var itemList: MutableList<Item>
    private lateinit var dataReference: FirebaseFirestore
    private var draft = ""
    private var messageId = ""
    private var status = false
    private var price = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        recyclerView = findViewById(R.id.recyclerView)
        remainView = findViewById(R.id.remainView)
        editText = findViewById(R.id.editTextText)
        plaintext = findViewById(R.id.inputText)
        confirmBtn = findViewById(R.id.confirmBtn)
        favBtn = findViewById(R.id.imageButton)
        backBtn = findViewById(R.id.backBtn)
        editBtn = findViewById(R.id.imageButton3)


        draft = intent.getStringExtra("draft").toString()

        itemList =  mutableListOf()

        dataReference = FirebaseFirestore.getInstance()

        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        remainView.doAfterTextChanged {
            price = remainView.text.toString()
        }
        editText.doAfterTextChanged {
            messageId = editText.text.toString()
        }
        editBtn.setOnClickListener {
            itemList.add(Item("",false))
            adapter.notifyDataSetChanged()
        }

        confirmBtn.setOnClickListener {
            var confirm = 1
            if(plaintext.text.isNotEmpty()) {
                for(item in itemList) {
                    if (!item.checked) {
                        confirm = 0
                    }
                }
                if(confirm==1){
                    plaintext.isEnabled = false
                    adapter.disable()
                    status = true
                    submitData()
                    val intent = Intent(this, ExpiryLog::class.java)
                    intent.putExtra("item", messageId)
                    startActivity(intent)
                }
            }
            else{
                Toast.makeText(this, "Please complete your shopping", Toast.LENGTH_SHORT).show()
            }
        }
        backBtn.setOnClickListener {
            if(adapter.itemCount!=0){
                submitData()
            }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        favBtn.setOnClickListener {
            favBtn.setImageResource(R.drawable.heart_full)
            addFav()
        }

        readFirestoreData()
        adapter = ShoppingListAdapter(itemList)
        recyclerView.adapter = adapter
        displayRemain()

    }
    private fun submitData(){

        var db = dataReference.collection("dataItemList")
        val itemCount = adapter.itemCount
        var line: String = ""
        var line2: String = ""
        for(item in 0 until itemCount!!){
            if(adapter.getName(item).isNotEmpty()) {
                line += adapter.getName(item)
                line2 += adapter.getChecked(item)
                if (item < itemCount - 1 && adapter.getName(item + 1).isNotEmpty()) {
                    line += ","
                    line2 += ","
                }
            }
        }
        val itemData = Message(messageId,System.currentTimeMillis().toString(),line,line2,plaintext.text.toString(),status)
        db.document(messageId).set(itemData)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Shopping list is saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(applicationContext, "Fail to save shopping list", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addFav(){

        var db = dataReference.collection("dataFavList")
        val itemData = Favourite(messageId)
        db.document(messageId).set(itemData)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Saved to favourite list", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(applicationContext, "Fail to save to favourite list", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayRemain(){
        var db = dataReference.collection("dataBudget")
        db.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    var budget = 0
                    val items = snapshot.toObjects(BudgetMessage::class.java)
                    for (item in items) {
                        budget = item.budget
                    }
                    remainView.text = budget.toString()
                }
            }
    }

    private fun readFirestoreData(){
        var db = dataReference.collection("dataItemList")
        db.get()
                .addOnSuccessListener { snapshot ->
                    if (snapshot != null) {
                        itemList.clear()
                        val items = snapshot.toObjects(Message::class.java)
                        for (item in items) {
                            if(item.id==draft) {
                                val names = item.message.split(",")
                                val checks = item.checked.split(",")
                                for (i in (names.indices)) {
                                    itemList.add(Item(names[i], checks[i].toBoolean()))
                                }
                                messageId = item.id
                                price = item.price
                                editText.text = messageId

                            }
                        }

                        adapter = ShoppingListAdapter(itemList)
                        recyclerView.adapter = adapter
                    }

                }
                .addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "Fail to read messages from Firestore",
                        Toast.LENGTH_SHORT
                    ).show()
                }

    }
    /*private fun startCounter(delay: Long){
        Thread{
            for(i in 0..delay/1000){
                runOnUiThread {
                    favBtn.setImageResource(R.drawable.heart_full)
                }
                Thread.sleep(1000)
            }
        }.start()
    }*/
}