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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.e_book.ViewModel.AppViewModel
import com.example.e_book.navigation.routs

@Composable
fun BookByCategory(navController: NavController, viewModel: AppViewModel = hiltViewModel(),category: String
                   ) {

    val state = viewModel.getBooksByCategoryState.collectAsState()
    val data = state.value.data ?: emptyList()

    LaunchedEffect(key1 = Unit) {
        viewModel.getBooksByCategory(category)
    }

    when{
        state.value.isLoading ->{
            CircularProgressIndicator()
        }
        !state.value.error.isNullOrEmpty()  ->{
            Box(modifier = Modifier.fillMaxSize().background(color = Color.Red), contentAlignment = Alignment.Center) {
                Text(text = state.value.error.toString())
            }
        }
        state.value.data.isNotEmpty() ->{

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(data){
                    BookItem(
                        title = it.BooksName,
                        author = it.Author,
                        bookImage = it.BookImage,
                        onItemClick = {
                            navController.navigate(routs.pdfView(it.bookUrl))
                        },
                    )
                }
            }

        }else -> {
        Text("No books found for this category.")
     }
    }
}