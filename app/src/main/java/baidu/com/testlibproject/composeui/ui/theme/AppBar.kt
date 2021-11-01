package baidu.com.testlibproject.composeui.ui.theme

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import baidu.com.testlibproject.R

@Composable
fun MyAppTopBar() {
    TopAppBar() {

        Row() {
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 20.sp,
                color = Color.White,
                fontStyle = FontStyle(
                    FontStyle.Italic.value
                )
            )
        }
    }
}