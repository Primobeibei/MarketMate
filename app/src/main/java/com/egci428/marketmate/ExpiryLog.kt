package com.egci428.marketmate

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

class ExpiryLog : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: LogAdapter
    private lateinit var itemList: MutableList<com.egci428.marketmate.Log>
    private lateinit var dataReference: FirebaseFirestore
    private lateinit var saveBtn: Button
    private lateinit var backBtn: ImageButton
    private var draft = ""
    private var messageId = ""

    private var uriKey: Uri? = null
    private var uriText: String = ""
    private lateinit var  outputDirectory: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expiry_log)

        draft = intent.getStringExtra("draft").toString()

        recyclerView = findViewById(R.id.recyclerView)
        saveBtn = findViewById(R.id.saveBtn)
        backBtn = findViewById(R.id.backBtn)

        itemList =  mutableListOf()

        dataReference = FirebaseFirestore.getInstance()

        val linearLayoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager

        saveBtn.setOnClickListener {
            submitData()
            val intent = Intent(this, ExpiryList::class.java)
            startActivity(intent)
        }
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("draft",messageId)
            startActivity(intent)
        }

        readFirestoreData()
        adapter = LogAdapter(itemList)
        recyclerView.adapter = adapter

    }

    private fun submitData(){

        val db = dataReference.collection("logItemList")
        val itemCount = adapter.itemCount
        var line: String = ""
        var line2: String = ""
        var line3: String = ""
        for(item in 0 until itemCount!!){
            if(adapter.getName(item).isNotEmpty()) {
                line += adapter.getName(item)
                line2 += adapter.getDate(item)
                line3 += adapter.getUri(item)
                if (item < itemCount - 1 && adapter.getName(item + 1).isNotEmpty()) {
                    line += ","
                    line2 += ","
                    line3 += ","
                }
            }
        }
        val itemData = LogMessage(messageId,System.currentTimeMillis().toString(),line,line2,line3)
        db.document(messageId).set(itemData)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Shopping list is saved successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(applicationContext, "Fail to save shopping list", Toast.LENGTH_SHORT).show()
            }
    }

    private fun readFirestoreData(){
        var db = dataReference.collection("dataItemList")
        db.get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null){
                    itemList.clear()
                    val items = snapshot.toObjects(Message::class.java)

                    for (item in items) {
                        if(item.id==draft) {
                            val names = item.message.split(",")
                            for (i in (names.indices)) {
                                itemList.add(Log(names[i],"",""))
                            }
                            messageId = item.id
                        }
                    }
                    adapter = LogAdapter(itemList)
                    recyclerView.adapter = adapter
                    Log.d("Link Success", "load data from firestore")
                }

            }
            .addOnFailureListener {
                Toast.makeText(applicationContext,"Fail to read messages from Firestore", Toast.LENGTH_SHORT).show()
            }

    }

    fun takePhoto(view: View){
        requestCameraPermission.launch(Manifest.permission.CAMERA)
    }

    private val requestCameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            isSuccess : Boolean ->
        if(isSuccess){
            Log.d("Take Picture", "Permission granted")
            takePicture()
        } else {
            Toast.makeText(applicationContext, "Camera has no permission", Toast.LENGTH_SHORT).show()
        }
    }

    private val captureImage = registerForActivityResult(ActivityResultContracts.TakePicture()){
        if(it){
            uriKey.let{
                    uri ->
                if(uri!=null){
                    uriKey = uri
                    Log.d("Capturte Image",uriKey.toString())
                    uriText = uriKey.toString()
                    itemList[adapter.pos].uri = uriText
                    adapter.getImage(LogViewHolder(adapter.getLogView()), uriKey!!)
                }
            }
        }
    }

    private fun takePicture(){
        uriKey = getTempFileUri()
        captureImage.launch(uriKey)
    }
    private fun getTempFileUri(): Uri {
        outputDirectory = getOutputDirectory(this)
        val tmpFile = File.createTempFile(
            SimpleDateFormat(FILENAME, Locale.ENGLISH).format(System.currentTimeMillis()), PHOTO_EXTENSION, outputDirectory).apply {
            createNewFile()
            deleteOnExit()
        }
        return FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.provider", tmpFile)
    }
    companion object {
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpeg"

        private fun getOutputDirectory(context: Context): File {
            val appContext = context.applicationContext
            val mediaDir = context.externalMediaDirs.firstOrNull()?.let {
                File(it, "MarketMate").apply { mkdirs() }
            }
            return if (mediaDir != null && mediaDir.exists())
                mediaDir else appContext.filesDir
        }
    }

}