package com.example.e_book.data.repo


import android.util.Log
import com.example.e_book.ResultState
import com.example.e_book.data.response.BookCategoryModel
import com.example.e_book.data.response.BookModels
import com.example.e_book.local.BookEntity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import com.google.firebase.database.getValue
import kotlinx.coroutines.channels.awaitClose

interface Repo  {


   suspend fun getAllBooks(): Flow<ResultState<List<BookModels>>>

   suspend fun getAllBooksCategory(): Flow<ResultState<List<BookCategoryModel>>>

   suspend fun getBookByCategory(category: String): Flow<ResultState<List<BookModels>>>

   suspend fun saveBook(book: BookEntity)
   suspend fun deleteBook(bookId: String)
   suspend fun getAllSavedBooks(): Flow<List<BookEntity>>

}