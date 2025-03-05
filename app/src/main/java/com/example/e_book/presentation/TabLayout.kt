package com.example.e_book.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabBar(navController: NavController) {

    val tabs = listOf(
        TabItem("Category", Icons.Rounded.Home),
        TabItem("All Books", Icons.Rounded.Book)
    )

    val pagerState = rememberPagerState(pageCount = {tabs.size})

   val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {

        TabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = Modifier.fillMaxWidth(),
            containerColor = Color.Cyan,
            contentColor = Color.Black
        ) {
            tabs.fastForEachIndexed { index, tab ->
                Tab(
                    selected = pagerState.currentPage == index,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                              },
                    text = { Text(text = tab.name)
                           },
                    icon = { Icon(imageVector = tab.icon, contentDescription = tab.name) }
                )
            }
        }
        HorizontalPager(state = pagerState) {
            when(it){
                0 -> category(navController = navController)
                1 -> AllBookScreen(navController = navController)
            }
        }
    }

}




data class TabItem(
    val name: String,
    val icon: ImageVector,
)

