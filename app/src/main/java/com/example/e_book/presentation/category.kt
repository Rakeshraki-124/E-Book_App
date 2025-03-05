package com.example.e_book.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.e_book.ViewModel.AppViewModel
import com.example.e_book.navigation.routs


@Composable
fun category(viewModel: AppViewModel = hiltViewModel(),navController: NavController) {

    val state = viewModel.getAllBooksCategoryState.collectAsState()


    LaunchedEffect(key1 = Unit) {
        viewModel.getAllBooksCategory()
    }

    when{
        state.value.isLoading ->{
            Text("IS LOADING")
        }

        state.value.error.isNotEmpty()  ->{
            Box(modifier = Modifier.fillMaxSize()){
                Text(text = "Error: ${state.value.error}")
            }
        }
        state.value.data.isNotEmpty() -> {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2), // 2 columns, adjust as needed
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Cyan),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.value.data.size) { category -> // Correct usage of items
                    BookCat(
                        name = state.value.data[category].name,
                        categoryImageUrl = state.value.data[category].categoryImageUrl,
                        onItemClick = {
                            navController.navigate(route = routs.BookByCategory(state.value.data[category].name))
                        }
                    )

                }
            }
        }
        else -> {
            Text("No categories found.")
        }
    }
}

@Composable
fun BookCat(
    categoryImageUrl: String,
    name: String,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(150.dp) // Fixed width for consistency
            .height(180.dp) // Fixed height for consistency
            .padding(4.dp)
            .clickable { onItemClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image (Square Shape)
            Box(
                modifier = Modifier
                    .size(100.dp) // Square size
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray) // Optional: Fallback background
            ) {
                AsyncImage(
                    model = categoryImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Category Name
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
    }
}