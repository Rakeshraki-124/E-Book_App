package com.example.e_book.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.e_book.R
import com.example.e_book.ViewModel.AppViewModel
import com.example.e_book.data.response.toBookEntity
import com.example.e_book.navigation.routs

@Composable
fun BookByCategory(
    navController: NavController,
    viewModel: AppViewModel = hiltViewModel(),
    category: String
) {
    val state = viewModel.getBooksByCategoryState.collectAsState()
    val savedBooks = viewModel.savedBookState.collectAsState()

    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.getBooksByCategory(category)
    }

    when {
        state.value.isLoading -> {
            CircularProgressIndicator()
        }
        !state.value.error.isNullOrEmpty() -> {
            Box(
                modifier = Modifier.fillMaxSize().background(color = Color.Red),
                contentAlignment = Alignment.Center
            ) {
                Text(text = state.value.error.toString())
            }
        }
        state.value.data.isNotEmpty() -> {
            LazyColumn(modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.Screen))) {
                items(
                    state.value.data,
                    key = { book ->
                        if (book.id.isEmpty()) {
                            "book-${book.BooksName}-${book.Author}"
                        } else {
                            book.id
                        }
                    }
                ) { book ->
                    BookItem(
                        title = book.BooksName,
                        author = book.Author,
                        bookImage = book.BookImage,
                        onItemClick = {
                            navController.navigate(routs.pdfView(book.bookUrl))
                        },
                        isSaved = savedBooks.value.any { it.id == book.id },
                        onSaveClick = {
                            viewModel.saveBook(book.toBookEntity())
                        },
                        onDeleteClick = {
                            viewModel.deleteBook(book.id)
                        }
                    )
                }
            }
        }
        else -> {
            Text("No books found for this category.")
        }
    }
}
