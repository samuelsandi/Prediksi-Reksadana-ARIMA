package com.ta.prediksireksadanaarima.views

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.ta.prediksireksadanaarima.R
import com.ta.prediksireksadanaarima.utilities.RDProductList
import com.ta.prediksireksadanaarima.viewModels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel = MainViewModel()
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.rd_list_view)

        val adapter = ArrayAdapter(this,
                                    android.R.layout.simple_list_item_1,
                                    viewModel.getProductList())
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle("Pilih jumlah hari prediksi")

            val picker = NumberPicker(this)
            picker.maxValue = 50
            picker.minValue = 1
            picker.wrapSelectorWheel = false
            builder.setView(picker)

            builder.setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(this@MainActivity, ChartActivity::class.java)
                intent.putExtra("rdCode", RDProductList.getList()[position].code)
                intent.putExtra("rdName", RDProductList.getList()[position].name)
                intent.putExtra("days", picker.value)
                startActivity(intent)
            }
            builder.setNegativeButton("Batal") { dialog, _ ->
                    dialog.cancel()
            }
            builder.show()
        }
    }
}