package com.ta.prediksireksadanaarima.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import com.ta.prediksireksadanaarima.R
import com.ta.prediksireksadanaarima.models.RDProductListModel
import com.ta.prediksireksadanaarima.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.rd_list_view)

        val adapter = ArrayAdapter(this,
                                    android.R.layout.simple_list_item_1,
                                    viewModel.getProductList())
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this@MainActivity, ChartActivity::class.java)
            intent.putExtra("rdCode", RDProductListModel.getList()[position].code)
            intent.putExtra("rdName", RDProductListModel.getList()[position].name)
            startActivity(intent)
        }
    }
}