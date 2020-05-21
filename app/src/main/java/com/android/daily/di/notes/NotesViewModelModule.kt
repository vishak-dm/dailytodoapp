package com.android.daily.di.notes

import androidx.lifecycle.ViewModel
import com.android.daily.di.ViewModelKey
import com.android.daily.viewModel.AddNotesViewModel
import com.android.daily.viewModel.MyThoughtsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class NotesViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddNotesViewModel::class)
    abstract fun bindsAddNotesViewModel(addNotesViewModel: AddNotesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MyThoughtsViewModel::class)
    abstract fun bindsMyThoughtsViewModel(myThoughtsViewModel: MyThoughtsViewModel): ViewModel
}