package com.example.e_book.presentation

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
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
import com.example.e_book.R


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
                    .background(colorResource(id = R.color.Screen)),
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
    // Animation for card elevation on hover
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val elevation = animateDpAsState(
        targetValue = if (isPressed.value) 12.dp else 6.dp,
        animationSpec = tween(durationMillis = 100)
    )

    Card(
        modifier = Modifier
            .width(160.dp) // Slightly wider for better spacing
            .height(200.dp) // Slightly taller for better spacing
            .padding(8.dp)
            .clickable(
                interactionSource = interactionSource,
                indication = null, // Disable default ripple effect
                onClick = onItemClick
            ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(elevation.value),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.Items)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Image (Square Shape)
            Box(
                modifier = Modifier
                    .size(120.dp) // Slightly larger image
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.2f)) // Fallback background
            ) {
                AsyncImage(
                    model = categoryImageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,

                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Category Name
            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}