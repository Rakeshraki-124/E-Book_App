package com.example.e_book.presentation.auth

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.e_book.R
import com.example.e_book.UserPreferences
import com.example.e_book.navigation.routs
import com.example.e_book.navigation.routs.SignInScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreenUI(navController: NavHostController) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var statusMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val userPreferences = UserPreferences(context)
    val scope = rememberCoroutineScope() // Added missing CoroutineScope

    val buttonScale by animateFloatAsState(
        targetValue = if (isLoading) 0.95f else 1f,
        animationSpec = tween(durationMillis = 100)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.signup),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.Black.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Sign Up",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(26.dp))
            val textFieldModifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(Color.White.copy(alpha = 0.9f), shape = RoundedCornerShape(12.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                shape = RoundedCornerShape(12.dp), // Ensures rounded corners
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White // Use this instead of background
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)) // Clips overflowing background
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                shape = RoundedCornerShape(12.dp), // Ensures rounded corners
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White // Use this instead of background
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)) // Clips overflowing background
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                shape = RoundedCornerShape(12.dp), // Ensures rounded corners
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = Color.White // Use this instead of background
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)) // Clips overflowing background
            )

            Spacer(modifier = Modifier.height(54.dp))
            if (statusMessage.isNotEmpty()) {
                Text(
                    text = statusMessage,
                    color = if (statusMessage.startsWith("Error")) Color.Red else Color.Green,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Button(
                onClick = {
                    if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                        isLoading = true
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { task ->
                                isLoading = false
                                statusMessage = if (task.isSuccessful) {

                                    val userId = auth.currentUser?.uid
                                    val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId!!)

                                    val user = mapOf("name" to name, "email" to email)
                                    userRef.setValue(user) // Store name and email in Firebase

                                    userRef.setValue(user)
                                        .addOnSuccessListener {
                                            Log.d("FirebaseSuccess", "User data stored successfully in Users section")
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("FirebaseError", "Error storing user data: ${e.message}")
                                        }



                                    Toast.makeText(
                                        context,
                                        "User registered: ${auth.currentUser?.email}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    navController.navigate(routs.SignInScreen)
                                    "User registered: ${auth.currentUser?.email}"
                                } else {
                                    "Error: ${task.exception?.message}"
                                }
                            }
                    } else {
                        statusMessage = "Please fill all fields"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .scale(buttonScale)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Create Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.White)) {
                        append("Already have an account? ")
                    }
                    withStyle(style = SpanStyle(color = Color.Cyan, fontWeight = FontWeight.Bold)) {
                        append("Sign In")
                    }
                },
                modifier = Modifier
                    .clickable { navController.navigate(routs.SignInScreen) }
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 8.dp)
            )

        }
    }
}
