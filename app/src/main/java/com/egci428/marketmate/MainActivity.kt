package com.egci428.marketmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var homeRecyclerView: RecyclerView
    lateinit var dataReference: FirebaseFirestore
    var mainArray: ArrayList<MainItem> = ArrayList()
    var mainAdapter = MainAdapter(mainArray)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeRecyclerView = findViewById(R.id.hmRecycler)

        dataReference = FirebaseFirestore.getInstance()

        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        homeRecyclerView.layoutManager = linearLayoutManager
        homeRecyclerView.adapter = mainAdapter

        val addButton = findViewById<Button>(R.id.button)
        val favButton = findViewById<ImageButton>(R.id.favBtn)
        val clockButton = findViewById<ImageButton>(R.id.clockBtn)
        val budButton = findViewById<ImageButton>(R.id.budBtn)
        val disButton = findViewById<ImageButton>(R.id.DisBtn)

        dataReference = FirebaseFirestore.getInstance()


        addButton.setOnClickListener{
            val intent = Intent(this, ShoppingList::class.java)
            startActivity(intent)
        }

        favButton.setOnClickListener{
            val intent = Intent(this, Favourite_page::class.java)
            startActivity(intent)
        }

        clockButton.setOnClickListener{
            val intent = Intent(this, ExpiryList::class.java)
            startActivity(intent)
        }


        budButton.setOnClickListener{
            val intent = Intent(this, Expense_management::class.java)
            startActivity(intent)
        }

        disButton.setOnClickListener{
            val intent = Intent(this, Discount_raffle::class.java)
            startActivity(intent)
        }

        readFirestoreData()
    }
    private fun readFirestoreData(){
        var db = dataReference.collection("dataItemList")
        db.orderBy("timeStamp").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null){
                    mainArray.clear()
                    val items = snapshot.toObjects(Message::class.java)
                    for (item in items) {
                        mainArray.add(MainItem(item.status,item.id))
                    }
                    mainAdapter = MainAdapter(mainArray)
                    homeRecyclerView.adapter = mainAdapter
                    //Log.d("Firestore Read", messages.toString())
                }

            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Fail to read messages from Firestore", Toast.LENGTH_SHORT).show()
            }

    }
}