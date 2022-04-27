package com.example.sunnyweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
/*全局获取context*/
class SunnyWeatherApplication : Application(){
    companion object{
        const val TIKEN = ""
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}