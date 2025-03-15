package com.example.e_book.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_books")
data class BookEntity(
    @PrimaryKey
    val id: String, // Use a unique identifier (e.g., book URL or ID)
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "bookImage") val bookImage: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "bookUrl") val bookUrl: String
)