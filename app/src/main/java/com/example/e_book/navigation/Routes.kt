package com.example.e_book.navigation
import android.os.Parcelable

import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.Serializable

@Serializable
sealed class routs {

    @Serializable
    object HomeScreen

    @Serializable
    object SavedBookScreen

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

