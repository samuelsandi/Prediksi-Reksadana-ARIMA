package com.ta.prediksireksadanaarima

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import okhttp3.*

class MainActivity : AppCompatActivity() {

    companion object MutualFundName{
        val msg = "RD13"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun sendMessage(view: View?) {
        val intent = Intent(this@MainActivity, Chart::class.java)
        intent.putExtra("mutualFundName",msg)
        startActivity(intent)
    }

}