package com.android.daily.di

import android.app.Application
import com.android.daily.DailyTodoApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
        modules = [AndroidSupportInjectionModule::class, ActivityBuildersModule::class,
            AppModule::class, ViewModelFactoryModule::class]
)
interface ApplicationComponent : AndroidInjector<DailyTodoApplication> {

    //Build the app component
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder

        fun build(): ApplicationComponent
    }
}