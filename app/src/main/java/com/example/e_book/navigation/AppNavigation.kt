package com.example.e_book.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.e_book.navigation.routs.SignInScreen
import com.example.e_book.navigation.routs.SignUpScreen
import com.example.e_book.presentation.BookByCategory
import com.example.e_book.presentation.TabBar
import com.example.e_book.presentation.auth.SignInScreenUI
import com.example.e_book.presentation.auth.SignUpScreenUI
import com.example.e_book.presentation.pdfView

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun AppNavigation(): NavHostController {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination =  routs.SignInScreen) {

    composable<routs.HomeScreen> {
        TabBar(navController)
    }
        composable<routs.pdfView>{
            val data = it.toRoute<routs.pdfView>()
            pdfView(pdf = data.pdfUrl)
        }
        composable<routs.BookByCategory>{
            val data = it.toRoute<routs.BookByCategory>()
            BookByCategory(navController,category = data.category)
        }
        composable< routs.SignInScreen>{
            SignInScreenUI(navController)
        }
        composable< routs.SignUpScreen>{
            SignUpScreenUI(navController)
        }
    }
    return navController
}

