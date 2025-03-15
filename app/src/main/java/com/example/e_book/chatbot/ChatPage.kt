package com.example.e_book.chatbot

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.e_book.ui.theme.Pink40
import com.example.e_book.ui.theme.Purple40

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun ChatPage(modifier: Modifier = Modifier, chatViewModel: ChatViewModel) {

    Column(modifier = modifier) {
        AppHeader()
        MessageList(modifier= Modifier.weight(1f),messageList = chatViewModel.messsageList)
        MessageInput(onMessageSent = {
            chatViewModel.sendMessage(it)
        })
    }
}

@Composable
fun MessageList(modifier: Modifier = Modifier, messageList: List<MessageModel>) {

    LazyColumn(
        modifier = modifier,
        reverseLayout = true
    ) {
        items(messageList.reversed()){
            // Text(text = it.message)
            MessageItem(message = it)
        }
    }
}

@Composable
fun MessageItem(message: MessageModel) {
    val isModel = message.role == "model"

    Row(
        verticalAlignment = Alignment.CenterVertically
    ){
        Box(
            modifier = Modifier.fillMaxWidth()
        ){
            Box(modifier = Modifier.align(if (isModel) Alignment.BottomStart else Alignment.BottomEnd)
                .padding(
                    start = if (isModel) 8.dp else 70.dp,
                    end = if (isModel) 70.dp else 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                )
                .clip(RoundedCornerShape(48f))
                .background(if (isModel) Pink40 else Purple40)
                .padding(16.dp)
            ) {
                Text(
                    text = message.message,
                    fontWeight = FontWeight.W500,
                    color = Color.White
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageInput(onMessageSent: (String) -> Unit) {
    var message by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            modifier = Modifier
                .weight(1f)
                .height(60.dp),
            value = message,
            onValueChange = { message = it },
            placeholder = { Text("Type a message...") },
            shape = RoundedCornerShape(25.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Gray,
                containerColor = MaterialTheme.colorScheme.background
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {
                if (message.isNotEmpty()) {
                    onMessageSent(message)
                    message = ""
                }
            },
            modifier = Modifier
                .size(50.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "Send",
                tint = Color.White
            )
        }
    }
}

@Composable
fun AppHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)) // Rounded bottom corners
            .background(MaterialTheme.colorScheme.primary)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Easy ChatBot",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
