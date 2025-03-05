package com.example.e_book

import android.util.Log
import androidx.compose.runtime.Composable
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun testFirebaseListener() {
    val firebaseDatabase = FirebaseDatabase.getInstance()
    firebaseDatabase.reference.child("Books").addValueEventListener(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (snapshot.exists()) {
                Log.d("FirebaseTest", "Data received: ${snapshot.value}")
            } else {
                Log.d("FirebaseTest", "No data found.")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("FirebaseTest", "Error: ${error.message}")
        }
    })
}