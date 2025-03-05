package com.example.e_book.data.EbookRepo

import android.util.Log
import com.example.e_book.ResultState
import com.example.e_book.data.repo.Repo
import com.example.e_book.data.response.BookCategoryModel
import com.example.e_book.data.response.BookModels
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class RepoImpl @Inject constructor(private val firebaseDatabase: FirebaseDatabase) : Repo {

    override suspend fun getAllBooks(): Flow<ResultState<List<BookModels>>> = callbackFlow {
        trySend(ResultState.Loading)

        val valueEvent = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("ChildSnapshot", "Snapshot received: ${snapshot.value}")
                if (snapshot.exists()) {
                    var items: List<BookModels> = emptyList()
                    Log.d("ChildSnapshot", "Data: ${snapshot.value}")

                    items = snapshot.children.map { value ->
                        value.getValue<BookModels>()!!

                    }
                    Log.d("ChildSnapshot", "Parsed items: $items")
                    trySend(ResultState.Success(items))

                } else {
                    Log.d("FirebaseError", "No data found at the specified path.")
                    trySend(ResultState.Error(Exception("No data found")))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                trySend(ResultState.Error(error.toException()))
                Log.d("Errror", "Error: ${error.message}")
            }

        }

        firebaseDatabase.reference.child("Books").addValueEventListener(valueEvent)

        awaitClose {
            firebaseDatabase.reference.removeEventListener(valueEvent)
            close()
        }

    }

    override suspend fun getAllBooksCategory(): Flow<ResultState<List<BookCategoryModel>>> =
        callbackFlow {

            trySend(ResultState.Loading)

            val valueCatEvent = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var items: List<BookCategoryModel> = emptyList()

                        items = snapshot.children.map { value ->
                            value.getValue<BookCategoryModel>()!!
                        }
                        trySend(ResultState.Success(items))
                    } else {
                        trySend(ResultState.Error(Exception("No data found")))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(ResultState.Error(error.toException()))
                }

            }

            firebaseDatabase.reference.child("BookCategory").addValueEventListener(valueCatEvent)

            awaitClose {
                firebaseDatabase.reference.removeEventListener(valueCatEvent)
                close()
            }

        }

    override suspend fun getBookByCategory(category: String): Flow<ResultState<List<BookModels>>> =
        callbackFlow {

            trySend(ResultState.Loading)

            val valueEvent = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var items: List<BookModels> = emptyList()

                        items = snapshot.children.filter {
                            it.getValue<BookModels>()!!.category == category
                        }.map {
                            it.getValue<BookModels>()!!
                        }
                        trySend(ResultState.Success(items))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    trySend(ResultState.Error(error.toException()))
                }
            }
            firebaseDatabase.reference.child("Books").addValueEventListener(valueEvent)

            awaitClose {
                firebaseDatabase.reference.removeEventListener(valueEvent)
                close()
            }
        }
}