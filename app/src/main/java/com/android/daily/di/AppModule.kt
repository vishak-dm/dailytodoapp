package com.android.daily.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

//Provide app level dependencies here
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideFirestoreInstance() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideFirestoreAuthInstance() = FirebaseAuth.getInstance()
}