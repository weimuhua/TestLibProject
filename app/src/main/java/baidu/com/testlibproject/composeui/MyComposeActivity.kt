package baidu.com.testlibproject.composeui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import baidu.com.testlibproject.composeui.ui.theme.TestLibProjectTheme
import coil.compose.rememberImagePainter

class MyComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestLibProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val columnSize = 100
                    val messages = mutableListOf<Message>()
                    for (i in 0 until columnSize) {
                        messages.add(i, Message("wayne", "this is my way"))
                    }
                    MessageList(messages)
                }
            }
        }
    }
}

@Composable
fun MessageCard(msg: Message) {
    Row(
        modifier = Modifier
            .padding(5.dp)
            .width(IntrinsicSize.Max)
    ) {
        Image(
            painter = rememberImagePainter("http://thirdqq.qlogo.cn/qqapp/101490787/2293D757B1A1F326A620663724D8E1BE/640"),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Column {
            Text(text = msg.author)
            Spacer(modifier = Modifier.size(5.dp))
            Text(text = msg.body)
        }
    }
}

@Composable
fun MessageList(messages: List<Message>) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(messages.size) { index ->
            MessageCard(msg = messages[index])
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMessageCard() {
    TestLibProjectTheme {
        MessageCard(Message("wayne", "this is my way"))
    }
}

data class Message(val author: String, val body: String)