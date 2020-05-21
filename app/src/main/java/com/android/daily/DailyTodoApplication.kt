package com.android.daily

import android.app.Application
import androidx.multidex.MultiDex
import com.android.daily.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import io.github.inflationx.calligraphy3.CalligraphyConfig
import timber.log.Timber.DebugTree
import timber.log.Timber


class DailyTodoApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
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