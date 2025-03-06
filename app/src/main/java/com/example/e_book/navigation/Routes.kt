package com.example.e_book.navigation
import kotlinx.serialization.Serializable

@Serializable
sealed class routs {

    @Serializable
    object HomeScreen

    @Serializable
    data class BookByCategory(
        val category: String
    )

    @Serializable
    data class pdfView(
        val pdfUrl: String
    )

    @Serializable
    object SignUpScreen

    @Serializable
    object SignInScreen

}

