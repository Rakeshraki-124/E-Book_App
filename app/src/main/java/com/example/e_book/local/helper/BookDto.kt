package com.example.e_book.local.helper

import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: String,
    val title: String,
    val bookImage: String,
    val author: String,
    val bookUrl: String,
    val localPath: String? = null
)