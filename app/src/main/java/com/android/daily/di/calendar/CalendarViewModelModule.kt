package com.android.daily.di.calendar

import androidx.lifecycle.ViewModel
import com.android.daily.di.ViewModelKey
import com.android.daily.viewModel.MyCalendarViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CalendarViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MyCalendarViewModel::class)
    abstract fun bindsMycalendarViewModel(myCalendarViewModel: MyCalendarViewModel): ViewModel
}