package com.ta.prediksireksadanaarima.viewModels

import androidx.lifecycle.ViewModel
import com.ta.prediksireksadanaarima.utilities.RDProductList

class MainViewModel: ViewModel() {
    private val model = RDProductList

    fun getProductList(): Array<String?> {
        val list = arrayOfNulls<String>(36)
        for (i in 0..35) {
            list[i] = model.getList()[i].name
        }
        return list
    }
}