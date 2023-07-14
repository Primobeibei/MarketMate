package com.egci428.marketmate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class Expense_management : AppCompatActivity() {

    lateinit var dataReference: FirebaseFirestore
    lateinit var budRecyclerView: RecyclerView
    var expenseArray: ArrayList<Expense> = ArrayList()
    var expenseAdapter = ExpenseAdapter(expenseArray)
    private lateinit var numView: TextView

    lateinit var inputText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_management)

        budRecyclerView = findViewById(R.id.budRecycler)
        numView = findViewById(R.id.numView)

        dataReference = FirebaseFirestore.getInstance()

        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        budRecyclerView.layoutManager = linearLayoutManager


        val favButton = findViewById<ImageButton>(R.id.favBtn)
        val clockButton = findViewById<ImageButton>(R.id.clockBtn)
        val homeButton = findViewById<ImageButton>(R.id.homeBtn)
        val disButton = findViewById<ImageButton>(R.id.DisBtn)
        val saveButton = findViewById<Button>(R.id.saveBtn)
        inputText = findViewById(R.id.inputText1)

        saveButton.setOnClickListener{
            submitData()
        }

        favButton.setOnClickListener{
            val intent = Intent(this, Favourite_page::class.java)
            startActivity(intent)
        }

        clockButton.setOnClickListener{
            val intent = Intent(this, ExpiryList::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


        disButton.setOnClickListener{
            val intent = Intent(this, Discount_raffle::class.java)
            startActivity(intent)
        }

        readFirestoreData()
        budRecyclerView.adapter = expenseAdapter
        displayBud()
        inputText.doAfterTextChanged {
            if(inputText.text != null){
                budgetCal()
            }
        }
    }

    private fun submitData(){
        budgetCal()
        var budget = numView.text.toString()
        if(budget.isEmpty()){
            inputText.error = "Please submit a budget"
            return
        }

        var db = dataReference.collection("dataBudget")
        val budgetData = BudgetMessage(budget.toInt())

        db.document("budget").set(budgetData)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Budget is saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(applicationContext, "Fail to save budget", Toast.LENGTH_SHORT).show()
            }
    }

    private fun readFirestoreData(){
        var db = dataReference.collection("dataItemList")
        db.orderBy("timeStamp").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null){
                    expenseArray.clear()
                    val messages = snapshot.toObjects(Message::class.java)
                    for (message in messages){
                        if (message.status) {
                            expenseArray.add(Expense(message.id,message.price.toInt()))
                        }
                    }
                    expenseAdapter = ExpenseAdapter(expenseArray)
                    budRecyclerView.adapter = expenseAdapter
                    //Log.d("Firestore Read", messages.toString())
                }

            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Fail to read messages from Firestore", Toast.LENGTH_SHORT).show()
            }

    }

    private fun displayBud(){
        var db = dataReference.collection("dataBudget")
        db.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    var budget = 0
                    val items = snapshot.toObjects(BudgetMessage::class.java)
                    for (item in items) {
                        budget = item.budget
                    }
                    numView.text = budget.toString()
                }
            }
    }

    private fun budgetCal(){
        var inputBud = numView.text.toString().toInt()
        var sum = 0
        var db = dataReference.collection("dataItemList")
        db.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    val items = snapshot.toObjects(Message::class.java)
                    for (item in items) {
                        if (item.status) {
                            var expense = item.price.toInt()
                            sum += expense
                        }
                    }
                }
            }

        var status = inputBud - sum
        numView.text = status.toString()
    }
}