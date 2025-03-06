package com.example.e_book.data.response

import com.example.e_book.local.BookEntity

data class BookModels(
    val id: String = "", // Add this field
    val Author: String = "",
    val BookImage: String = "",
    val BooksName: String = "",
    val bookUrl: String = "",
    val category: String = "",
)

fun BookModels.toBookEntity(): BookEntity {
    return BookEntity(
        id = this.id,
        title = this.BooksName,
        author = this.Author,
        bookImage = this.BookImage,
        bookUrl = this.bookUrl
    )
}