package com.example.e_book.Utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer

@Composable
fun BookCatShimmerEffect() {
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .padding(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.elevatedCardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.LightGray),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Image Placeholder
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .shimmerEffect() // ✅ Apply shimmer effect
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Text Placeholder
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .width(100.dp)
                    .shimmerEffect() // ✅ Apply shimmer effect
            )
        }
    }
}

fun Modifier.shimmerEffect(): Modifier {
    return this
        .background(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color.Gray.copy(alpha = 0.3f),
                    Color.Gray.copy(alpha = 0.1f),
                    Color.Gray.copy(alpha = 0.3f)
                ),
                start = Offset(0f, 0f),
                end = Offset(300f, 300f)
            )
        )
}

