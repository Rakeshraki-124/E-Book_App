package com.example.e_book.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.e_book.UserPreferences
import com.example.e_book.navigation.routs.SignInScreen
import com.example.e_book.navigation.routs.SignUpScreen
import com.example.e_book.presentation.BookByCategory
import com.example.e_book.presentation.SavedBookScreen
import com.example.e_book.presentation.TabBar
import com.example.e_book.presentation.auth.SignInScreenUI
import com.example.e_book.presentation.auth.SignUpScreenUI
import com.example.e_book.presentation.pdfView.pdfView
import com.example.e_book.rememberNetworkStatus

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavigation(): NavHostController {

    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val isLoggedIn = userPreferences.isLoggedIn.collectAsState(initial = false)


    val isConnected = rememberNetworkStatus(context) // ✅ Check internet status
    val navController = rememberNavController()

    val startDestination = when {
        !isConnected && isLoggedIn.value -> routs.SavedBookScreen // ✅ Show only saved books if offline
        isLoggedIn.value -> routs.HomeScreen
        else -> SignInScreen
    }

    NavHost(navController = navController, startDestination =  startDestination) {

    composable<routs.HomeScreen> {
        TabBar(navController)
    }
        composable<routs.pdfView>{
            val data = it.toRoute<routs.pdfView>()
            pdfView(
                pdf = data.pdfUrl
            )
        }
        composable<routs.BookByCategory>{
            val data = it.toRoute<routs.BookByCategory>()
            BookByCategory(navController,category = data.category)
        }
        composable< SignInScreen>{
            SignInScreenUI(navController)
        }
        composable< SignUpScreen>{
            SignUpScreenUI(navController)
        }
        composable< routs.SavedBookScreen>{
            SavedBookScreen(
                navController = navController
            )
        }
    }
    return navController
}

