package com.android.daily

import android.app.Application
import android.support.multidex.MultiDex
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.calligraphy3.CalligraphyConfig
import timber.log.Timber.DebugTree
import timber.log.Timber


class DailyTodoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                                .setDefaultFontPath("fonts/Avenir-Book.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build())

        Timber.plant(DebugTree())
    }
}