package com.egci428.marketmate

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlin.random.Random

class Discount_raffle : AppCompatActivity(),SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var lastUpdate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discount_raffle)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lastUpdate = System.currentTimeMillis()


        val favButton = findViewById<ImageButton>(R.id.favBtn)
        val clockButton = findViewById<ImageButton>(R.id.clockBtn)
        val homeButton = findViewById<ImageButton>(R.id.homeBtn)
        val budButton = findViewById<ImageButton>(R.id.budBtn)



        favButton.setOnClickListener {
            val intent = Intent(this, Favourite_page::class.java)
            startActivity(intent)
        }

        clockButton.setOnClickListener {
            //val intent = Intent(this, Expirydatelist::class.java)
            startActivity(intent)
        }

        homeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        budButton.setOnClickListener {
            val intent = Intent(this, Expense_management::class.java)
            startActivity(intent)
        }

    }

    private fun getAccelerometer(event: SensorEvent) {
        var discount: Int = Random.nextInt(0, 5)
        val coupon = findViewById<ImageView>(R.id.imageView)
        val couponCode = findViewById<TextView>(R.id.codeView)
        val values = event.values

        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accel =
            (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)
        val actualTime = System.currentTimeMillis()
        Log.d("String",accel.toString())


        if (accel >= 2) {
            //Log.d("String",accel.toString())
            if (actualTime - lastUpdate < 200) {
                return
            }

            lastUpdate = actualTime
            if (discount == 0) {
                coupon.setImageResource(R.drawable.dis_10)
                couponCode.text= "blkjhgfdr6"
            } else if (discount == 1) {
                coupon.setImageResource(R.drawable.dis_15)
                couponCode.text = "dfkylsdwer"
            } else if (discount == 2) {
                coupon.setImageResource(R.drawable.dis_20)
                couponCode.text = "swdefrgthj"
            } else if (discount == 3) {
                coupon.setImageResource(R.drawable.dis_25)
                couponCode.text = "stipoiyuty"
            } else if (discount == 4) {
                coupon.setImageResource(R.drawable.dis_30)
                couponCode.text = "otrddhjtfk"
            } else if (discount == 5) {
                coupon.setImageResource(R.drawable.dis_40)
                couponCode.text = "gdfhjgkjhl"
            }
        }

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                getAccelerometer(event)
            }
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }
    override fun onPause(){
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }

    override fun onResume(){
        super.onResume()
        sensorManager!!.registerListener(this,sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL)
    }
}
