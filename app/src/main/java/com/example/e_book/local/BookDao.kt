package com.example.e_book.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.RoomWarnings
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Update
    suspend fun saveBook(book: BookEntity)

    @Query("DELETE FROM saved_books WHERE id = :bookId")
    suspend fun deleteBook(bookId: String)

    @Query("SELECT * FROM saved_books")
    fun getAllBooks(): Flow<List<BookEntity>>


}