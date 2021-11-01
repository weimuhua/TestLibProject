package baidu.com.testlibproject.composeui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import baidu.com.testlibproject.composeui.ui.theme.MyAppTopBar
import baidu.com.testlibproject.composeui.ui.theme.TestLibProjectTheme
import coil.compose.rememberImagePainter


class ComposeMainActivity : ComponentActivity() {

    companion object {
        private const val LAZY_COLUMN_ACTIVITY = "LazyColumn"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val listBean = ActivityBean(LAZY_COLUMN_ACTIVITY) {
            onClick(LAZY_COLUMN_ACTIVITY)
        }
        val activityBeans = mutableListOf<ActivityBean>().apply {
            add(listBean)
        }

        setContent {
            TestLibProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Scaffold(topBar = {
                        MyAppTopBar()
                    }) {
                        ActivityList(activityBeans = activityBeans)
                    }
                }
            }
        }
    }

    private fun onClick(title: String) {
        if (LAZY_COLUMN_ACTIVITY == title) {
            startActivity(Intent(this, MyComposeActivity::class.java))
        }
    }

    @Composable
    fun ActivityList(activityBeans: List<ActivityBean>) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(activityBeans.size) { index ->
                ActivityItem(
                    title = activityBeans[index].title,
                    click = activityBeans[index].click
                )
            }
        }
    }

    @Composable
    fun ActivityItem(title: String, click: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .clickable(onClick = click)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(39.dp),
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                )
            }

            Divider(
                modifier = Modifier.padding(horizontal = 5.dp),
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f)
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewActivityItem() {
        TestLibProjectTheme {
            ActivityItem("ComposeList") {}
        }
    }

    data class ActivityBean(val title: String, val click: () -> Unit)
}