package com.ta.prediksireksadanaarima.viewModels

import android.widget.ListView
import androidx.lifecycle.ViewModel
import com.ta.prediksireksadanaarima.models.RDProductListModel

class MainViewModel: ViewModel() {
    private val model = RDProductListModel

    fun getProductList(): Array<String?> {
        val list = arrayOfNulls<String>(36)
        for (i in 0..35) {
            list[i] = model.getList()[i].name
        }
        return list
    }
}