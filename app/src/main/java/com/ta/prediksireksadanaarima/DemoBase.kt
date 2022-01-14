package com.ta.prediksireksadanaarima

import androidx.appcompat.app.AppCompatActivity
import android.graphics.Typeface
import android.os.Bundle

open class DemoBase : AppCompatActivity() {
    protected val months = arrayOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    )
    private var tfRegular: Typeface? = null
    private var tfLight: Typeface? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tfRegular = Typeface.createFromAsset(assets, "OpenSans-Regular.ttf")
        tfLight = Typeface.createFromAsset(assets, "OpenSans-Light.ttf")
    }
}