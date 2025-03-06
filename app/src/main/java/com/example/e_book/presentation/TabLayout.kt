package com.example.e_book.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Chat
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import com.example.e_book.R
import com.example.e_book.chatbot.ChatPage
import com.example.e_book.chatbot.ChatViewModel


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabBar(navController: NavController) {
    val tabs = listOf(
        TabItem("Category", iconVector = Icons.Rounded.Home),
        TabItem("All Books", iconVector = Icons.Rounded.Book),
        TabItem("SavedBook", iconVector = Icons.Rounded.Image),
        TabItem("ChatBot", iconPainter = painterResource(id = R.drawable.rakii)) // Custom PNG icon
    )

    val chatViewModel = ChatViewModel()
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.Screen))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(2.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = containerColor)
        ) {
            TabRow(
                selectedTabIndex = pagerState.currentPage,
                modifier = Modifier.fillMaxWidth(),
                containerColor = colorResource(id = R.color.Tab),
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(text = tab.title) },
                        icon = {
                            tab.iconVector?.let {
                                Icon(imageVector = it, contentDescription = tab.title)
                            }
                            tab.iconPainter?.let {
                                Icon(
                                    painter = it,
                                    contentDescription = tab.title,
                                    modifier = Modifier.size(24.dp) // Adjust size if needed
                                )
                            }
                        }
                    )
                }
            }
        }

        HorizontalPager(state = pagerState) {
            when (it) {
                0 -> category(navController = navController)
                1 -> AllBookScreen(navController = navController)
                2 -> SavedBookScreen(navController = navController)
                3 -> ChatPage(chatViewModel = chatViewModel)
            }
        }
    }
}

data class TabItem(
    val title: String,
    val iconVector: ImageVector? = null, // Built-in Material icons
    val iconPainter: Painter? = null // Custom drawable icons
)

