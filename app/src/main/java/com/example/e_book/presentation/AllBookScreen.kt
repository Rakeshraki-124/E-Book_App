package com.example.e_book.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowOutward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.e_book.R

import com.example.e_book.ViewModel.AppViewModel
import com.example.e_book.data.response.toBookEntity
import com.example.e_book.navigation.routs

@Composable
fun AllBookScreen(viewModel: AppViewModel = hiltViewModel(), navController: NavController) {
    val state = viewModel.getAllBookState.collectAsState()
    val savedBooks = viewModel.savedBookState.collectAsState()

    val context = LocalContext.current


    LaunchedEffect(Unit) {
        viewModel.getAllBooks()
    }

    when {
        state.value.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.value.error.isNotEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.value.error}", color = Color.Red)
            }
        }

        state.value.data.isNotEmpty() -> {
            Log.d("AllBookData", "Data: ${state.value.data}")
            LazyColumn(
                modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.Screen)),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(
                    state.value.data,
                    key = { book ->
                        if (book.id.isEmpty()) {
                            // Fallback key for empty IDs
                            "book-${book.BooksName}-${book.Author}"
                        } else {
                            book.id
                        }
                    }
                    ) { book ->
                    BookItem(
                        title = book.BooksName,
                        bookImage = book.BookImage,
                        author = book.Author,
                        isSaved = savedBooks.value.any { it.id == book.id }, // Check if the book is saved
                        onItemClick = {
                            navController.navigate(routs.pdfView(book.bookUrl))
                        },
                        onSaveClick = {
                            Log.d("SaveBook", "Saving book: ${book.BooksName} (ID: ${book.id})")
                            viewModel.saveBook(book.toBookEntity()) // Save the book
                        },
                        onDeleteClick = {
                            viewModel.deleteBook(book.id) // Delete the book
                        }
                    )
                }
            }
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No Books Available", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun BookItem(
    title: String,
    bookImage: String,
    author: String,
    isSaved: Boolean,
    onItemClick: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit = {}

) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .clickable(onClick = onItemClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp, pressedElevation = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxHeight()
                .padding(0.dp)
                .background(colorResource(id = R.color.Items)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book Image
            AsyncImage(
                model = bookImage,
                contentDescription = title,
                modifier = Modifier
                    .width(130.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Book Details
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Author: $author",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }


            // Save Button
            IconButton(
                onClick = {
                    if (isSaved) {
                        onDeleteClick() // Remove from saved books
                    } else {
                        onSaveClick() // Save the book
                    }
                },
                modifier = Modifier.padding(end = 8.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            ) {
                Icon(
                    imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = if (isSaved) "Saved" else "Save",
                    tint = MaterialTheme.colorScheme.primary
                )
            }


            // Open Book Icon
            IconButton(
                onClick = onItemClick,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowOutward, // More intuitive icon
                    contentDescription = "Open Book",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
