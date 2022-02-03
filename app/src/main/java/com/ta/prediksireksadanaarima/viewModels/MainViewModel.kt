package com.ta.prediksireksadanaarima.viewModels

import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import com.ta.prediksireksadanaarima.models.RDProductListModel
import com.ta.prediksireksadanaarima.views.MainActivity

class MainViewModel: ViewModel() {
    private val model = RDProductListModel

    fun getProductList(activity: MainActivity){
        val list = arrayOfNulls<String>(36)
        for (i in 0..35) {
            list[i] = model.getList()[i].name
        }
        val adapter = ArrayAdapter(activity, android.R.layout.simple_list_item_1, list)
        activity.listView.adapter = adapter
    }
}