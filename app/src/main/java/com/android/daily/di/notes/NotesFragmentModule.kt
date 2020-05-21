package com.android.daily.di.notes

import com.android.daily.ui.AddNotesFragment
import com.android.daily.ui.MyThoughtsFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class NotesFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributesAddNotesFragment(): AddNotesFragment

    @ContributesAndroidInjector
    abstract fun contributesMyThoughtFragment(): MyThoughtsFragment
}