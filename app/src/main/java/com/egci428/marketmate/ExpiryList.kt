package com.egci428.marketmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ExpiryList : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dataReference: FirebaseFirestore
    private lateinit var expiryList: MutableList<Log>
    private lateinit var adapter: ExpiryListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expiry_list)

        expiryList = mutableListOf()

        recyclerView = findViewById(R.id.exRecycler)

        dataReference = FirebaseFirestore.getInstance()

        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        val favButton = findViewById<ImageButton>(R.id.favBtn)
        val budButton = findViewById<ImageButton>(R.id.budBtn)
        val disButton = findViewById<ImageButton>(R.id.DisBtn)
        val homeButton = findViewById<ImageButton>(R.id.homeBtn)


        favButton.setOnClickListener{
            val intent = Intent(this, Favourite_page::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
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
        adapter = ExpiryListAdapter(expiryList)
        recyclerView.adapter = adapter

    }
    private fun readFirestoreData(){
        val db = dataReference.collection("logItemList")
        db.orderBy("timeStamp").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null){
                    expiryList.clear()
                    val items = snapshot.toObjects(LogMessage::class.java)

                    for (item in items) {
                        val names = item.message.split(",")
                        val dates = item.date.split(",")
                        val uris = item.uri.split(",")
                        for (i in (names.indices)) {
                            expiryList.add(Log(names[i],dates[i],uris[i]))
                        }
                    }
                    adapter = ExpiryListAdapter(expiryList)
                    recyclerView.adapter = adapter
                    android.util.Log.d("Link Success", "load data from firestore")
                }

            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Fail to read messages from Firestore", Toast.LENGTH_SHORT).show()
            }

    }
}