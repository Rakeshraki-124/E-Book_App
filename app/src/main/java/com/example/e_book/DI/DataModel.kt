package com.example.e_book.DI

import android.util.Log
import com.example.e_book.data.EbookRepo.RepoImpl
import com.example.e_book.data.repo.Repo
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataModel {

    @Provides
    @Singleton
    fun ProvideFirebaseRealtimeDatabase(): FirebaseDatabase {
        Log.d("Firebase","Insitializing Firebase Realtime Database")
        return FirebaseDatabase.getInstance()
    }

    @Provides
    @Singleton
    fun ProvideRepo(firebaseDatabase: FirebaseDatabase): Repo {
        Log.d("Firebase","Providing Repo")
        return RepoImpl(firebaseDatabase)
    }
}