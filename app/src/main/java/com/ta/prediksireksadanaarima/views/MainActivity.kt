package com.ta.prediksireksadanaarima.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.ta.prediksireksadanaarima.R
import com.ta.prediksireksadanaarima.utilities.RDProductList

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.rd_list_view)
        val list = arrayOfNulls<String>(36)
        for (i in 0..35) {
            list[i] = RDProductList.getList()[i][1]
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        listView.adapter = adapter
        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@MainActivity, ChartActivity::class.java)
            intent.putExtra("rdCode", RDProductList.getList()[position][0])
            intent.putExtra("rdName", RDProductList.getList()[position][1])
            startActivity(intent)
        }
    }
}