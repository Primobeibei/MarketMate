package com.egci428.marketmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.egci428.marketmate.FavouriteAdapter
import com.egci428.marketmate.Favourite
import com.google.firebase.firestore.FirebaseFirestore

class Favourite_page : AppCompatActivity() {

    private lateinit var favlist : MutableList<Favourite>
    lateinit var dataReference: FirebaseFirestore
    private lateinit var favRecycler: RecyclerView
    lateinit var favouriteAdapter: FavouriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite_page)

        dataReference = FirebaseFirestore.getInstance()

        favRecycler = findViewById(R.id.favRecycler)

        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        favRecycler.layoutManager = linearLayoutManager


        val clockButton = findViewById<ImageButton>(R.id.clockBtn)
        val homeButton = findViewById<ImageButton>(R.id.homeBtn)
        val budButton = findViewById<ImageButton>(R.id.budBtn)
        val disButton = findViewById<ImageButton>(R.id.DisBtn)

        favlist = mutableListOf()


        clockButton.setOnClickListener{
            val intent = Intent(this, ExpiryList::class.java)
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

        favouriteAdapter = FavouriteAdapter(favlist)
        favRecycler.adapter = favouriteAdapter

    }
    private fun readFirestoreData(){
        var db = dataReference.collection("dataFavList")
        db.orderBy("timeStamp").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null){
                    favlist.clear()
                    val messages = snapshot.toObjects(Favourite::class.java)
                    for (message in messages){
                        favlist.add(message)
                    }
                    favouriteAdapter = FavouriteAdapter(favlist)
                    favRecycler.adapter = favouriteAdapter
                    //Log.d("Firestore Read", messages.toString())
                }

            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Fail to read messages from Firestore", Toast.LENGTH_SHORT).show()
            }

    }
}