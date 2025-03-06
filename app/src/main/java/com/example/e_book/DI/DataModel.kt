package com.example.e_book.DI

import android.util.Log
import androidx.room.Room
import com.example.e_book.data.EbookRepo.RepoImpl
import com.example.e_book.data.repo.Repo
import com.example.e_book.local.AppDatabase
import com.example.e_book.local.BookDao
import com.google.firebase.database.FirebaseDatabase
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun ProvideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        Log.d("Room", "Providing Room Database")
        return Room.databaseBuilder(
            context, // Use the injected context
            AppDatabase::class.java,
            "book_database"
        ).build()
    }


    @Provides
    @Singleton
    fun ProvideBookDao(appDatabase: AppDatabase): BookDao {
        Log.d("Room", "Providing BookDao")
        return appDatabase.bookDao()
    }

    @Provides
    @Singleton
    fun ProvideRepo(firebaseDatabase: FirebaseDatabase,bookDao: BookDao): Repo {
        Log.d("Firebase","Providing Repo")
        return RepoImpl(firebaseDatabase,bookDao)
    }
}